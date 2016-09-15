package controllers;

import models.User;
import play.data.binding.Binder;
import play.exceptions.TemplateNotFoundException;
import play.libs.Crypto;
import play.mvc.With;

import java.lang.reflect.Constructor;

@Check("admin")
@With(Secure.class)
public class Users extends CRUD {
    public static void create() throws Exception {
        // Get model type
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type); // render not found error if framework can't determine model type
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Create new instance of model
        User user = (User) constructor.newInstance();
        // Bind all parameter value from submitted form
        Binder.bindBean(params.getRootParamNode(), "object", user);
        // Hash the password
        user.setPassword(Crypto.passwordHash(user.getEmail() + user.getPassword()));

        // Check validity of model
        validation.valid(user);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/blank.html", type, user);
            } catch (TemplateNotFoundException e) {
                render("CRUD/blank.html", type, user);
            }
        }
        user._save(); // Finally, save the model
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        if (params.get("_saveAndAddAnother") != null) {
            redirect(request.controller + ".blank");
        }
        redirect(request.controller + ".show", user._key());
    }

    /**
     * Re-implement Update (U) mehod
     * @param id
     * @throws Exception
     */
    public static void save(String id) throws Exception {
        // Get model type
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type); // render not found error if framework can't determine model type

        // Find the model to be updated
        User user = (User) type.findById(id);
        notFoundIfNull(user); // render not found error if framework can't determine model record
        // Bind all parameter value from submitted form
        Binder.bindBean(params.getRootParamNode(), "object", user);
        // Hash the password
        user.setPassword(Crypto.passwordHash(user.getEmail() + user.getPassword()));

        // Check validity of model
        validation.valid(user);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/show.html", type, user);
            } catch (TemplateNotFoundException e) {
                render("CRUD/show.html", type, user);
            }
        }
        user._save(); // Finally, save changes
        flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        redirect(request.controller + ".show", user._key());
    }
}
