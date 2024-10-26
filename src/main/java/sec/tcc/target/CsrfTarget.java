package sec.tcc.target;

import sec.tcc.Target;

public class CsrfTarget extends Target {

    public CsrfTarget() {
        super("Csrf", new String[] {"csrf", "configuration"}, new String[]{"csrf.*disable", "csrf\\(\\)\\.disable\\(\\)"});
    }

}
