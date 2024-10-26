package sec.tcc.target;

import sec.tcc.Target;

public class SqlInjectionTarget extends Target {
    public SqlInjectionTarget() {
        super("SQL Injection", new String[] {"@query"}, new String[] {
                "\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\\+[^\"\\\\]*(\\\\.[^\"\\\\]*)*\".*(select|insert|update|delete|drop|;)",
                "createStatement\\(\\)",
                "execute\\(.*\"",
                "execute(Query|Update)\\(.*\\+.*\\)"});
    }
}
