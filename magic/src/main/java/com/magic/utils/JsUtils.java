package com.magic.utils;

import org.springframework.util.StringUtils;

import javax.script.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by xm on 2017/11/9.
 */
public class JsUtils {

    public static Object eval(String expression, Map<String, Object> params) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        ScriptContext ctx = new SimpleScriptContext();
        Bindings scope = ctx.getBindings(ScriptContext.ENGINE_SCOPE);
        for (Map.Entry<String, Object> e : params.entrySet()) {
            scope.put(e.getKey(), e.getValue());
        }

        try {
            Object ret = engine.eval(expression, scope);
            return ret;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
}
