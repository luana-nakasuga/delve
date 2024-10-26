package sec.tcc.target;

import sec.tcc.Target;

public class SqlInjectionTarget extends Target {
    public SqlInjectionTarget() {
        super("SQL Injection", new String[] {"@query"});
    }
}
