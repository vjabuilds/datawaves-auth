package dev.vjabuilds.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import dev.vjabuilds.models.DatawavesUser;
import dev.vjabuilds.repos.UsersRepo;
import dev.vjabuilds.view_models.LoginModel;
import dev.vjabuilds.view_models.RegistrationModel;

import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;

@Path("/users")
@AllArgsConstructor
@PermitAll
public class UserResource {

    UsersRepo usersRepo;
    @Inject JsonWebToken jwt;
    @Inject JWTParser parser;
    @Inject JWTAuthContextInfo authContextInfo;

    @GET
    @RolesAllowed({"admin"})
    public Uni<List<DatawavesUser>> users(){
       return usersRepo.getUsers();
    }

    @POST
    public Uni<Response> register_user(RegistrationModel model) {
        return usersRepo.registerUser(model).map(x -> x ? 
            Response.ok().build() : Response.status(409).build()
        );
    }

    @POST
    @Path("/login")
    public Uni<Response> login(LoginModel model) {
        return usersRepo.login(model).map(
            x -> x != null ? Response.ok(x.auth())
                    .cookie(
                        new NewCookie("refresh_token", 
                        x.refresh(), 
                        null, null, 
                        NewCookie.DEFAULT_VERSION, 
                        null, 
                        NewCookie.DEFAULT_MAX_AGE, 
                        null, 
                        false, 
                        true)
                    ).build() 
                    :
                    Response.status(401, "Invalid username and password combiantion")
                    .build()

        );
    }

    @GET
    @Path("/refresh")
    public Uni<Response> refresh(@CookieParam("refresh_token") Cookie cookie) {
        String refresh_token = cookie.getValue();
        try {
            var token = parser.verify(refresh_token, authContextInfo.getPublicVerificationKey());

            if(token.getAudience().size() != 1)
                return createErrorWithMessage("Token must contain exactly 1 audience!");

            if(!token.getAudience().toArray()[0].equals(usersRepo.getAudience()))
                return createErrorWithMessage("Token audience not set to correct URL" + token.getAudience().toArray()[0]);

            return usersRepo.refresh(token).map(x -> Response.ok(x).build());
        } catch(ParseException e) {
            return createErrorWithMessage(e.toString());
        }
    }

    private Uni<Response> createErrorWithMessage(String msg)
    {
        return Uni.createFrom().item(Response.status(400).entity(msg).build());
    }
}
