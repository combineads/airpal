package com.airbnb.airpal.core;

import com.airbnb.airpal.presto.Table;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;

import static java.lang.String.format;

public class AuthorizationUtil
{
    public static boolean isAuthorizedRead(AirpalUser subject, Table table)
    {
        boolean isaccess= false;
        String access = subject.getAccessLevel();
        if(access.equalsIgnoreCase("Data-owner")|| access.equalsIgnoreCase("data-scientist"))
        {
         isaccess= true;
        }
       return isaccess;
        //return isAuthorizedRead(subject, table.getConnectorId(), table.getSchema(), table.getTable());
    }

    public static boolean isAuthorizedRead(AirpalUser subject, String connectorId, String schema, String table)
    {
         boolean isaccess= false;
        String access = subject.getAccessLevel();
        if(access.equalsIgnoreCase("Data-owner")|| access.equalsIgnoreCase("data-scientist"))
        {
         isaccess= true;
        }
       return isaccess;
       // return subject.isPermitted(format("read:%s.%s:%s", connectorId, schema, table));
    }

    public static boolean isAuthorizedWrite(AirpalUser subject, String connectorId, String schema, String table)
    {   
        boolean isaccess= false;
        String access = subject.getAccessLevel();
        if(access.equalsIgnoreCase("Data-owner"))
        {
         isaccess= true;
        }else{
          isaccess=false;
        }
        
       return isaccess;
      
      //  return subject.isPermitted(format("write:%s.%s:%s", connectorId, schema, table));
    }

    public static class AuthorizedTablesPredicate
            implements Predicate<Table>
    {
        private final AirpalUser subject;

        public AuthorizedTablesPredicate(AirpalUser subject)
        {
            this.subject = subject;
        }

        @Override
        public boolean apply(@Nullable Table input)
        {
            if (input == null) {
                return false;
            }

            return isAuthorizedRead(subject, input);
        }
    }
}

