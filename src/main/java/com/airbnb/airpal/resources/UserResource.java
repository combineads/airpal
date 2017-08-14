package com.airbnb.airpal.resources;

import com.airbnb.airpal.core.AirpalUser;
import com.airbnb.airpal.core.AirpalUserImpl;
import com.airbnb.airpal.core.AuthorizationUtil;
import lombok.Value;
import org.secnod.shiro.jaxrs.Auth;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
  private final String defaultCatalog;
  @Inject
  public UserResource(@Named("default-catalog") final String defaultCatalog)
  {
     this.defaultCatalog = defaultCatalog; 
  }  
  @GET
  public Response getUserInfo(@Auth AirpalUser user,@QueryParam("catalog") Optional<String> catalogOptional) {
    if (user == null) {
      return Response.status(Response.Status.FORBIDDEN).build();
    } else {
      String schema=user.getDefaultSchema();
      final String catalog = catalogOptional.or(defaultCatalog);
      log.info("catalog for getting tables: isss======= schema1====  "+catalog);
      
      return Response.ok(
        new UserInfo(
          user.getUserName(),
          new ExecutionPermissions(
            AuthorizationUtil.isAuthorizedWrite(user, catalog, schema, "any"),
            AuthorizationUtil.isAuthorizedWrite(user, catalog, schema, "any"),
            user.getAccessLevel())
        )).build();
    }
  }
    
    @Value
    private static class UserInfo
    {   
        @JsonProperty
        private final String name;
        @JsonProperty
        private final ExecutionPermissions executionPermissions;
    }

    @Value
    public static class ExecutionPermissions
    {
        @JsonProperty
        private final boolean canCreateTable;
        @JsonProperty
        private final boolean canCreateCsv;
        @JsonProperty
        private final String accessLevel;
    }
}

