package com.compilers.lox;

class Interpreter implements Expr.Visitor<Object> {
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
      return evaluate(expr.expression);
    }
    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
      Object right = evaluate(expr.right);
  
      switch (expr.operator.type) {
        case BANG:
            return !isTruthy(right);    
        case MINUS:
            checkNumberOperand(expr.operator,right);
            return -(double)right;
        default:
            return null;
      }
  
      
    }
    @Override
    public Object visitBinaryExpr(Expr.Binary expr){
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch(expr.operator.type){
            case MINUS:
                checkNumberOperand(expr.operator,left, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperand(expr.operator,left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperand(expr.operator,left, right);
                return (double)left * (double)right;
            case PLUS:
                if(left instanceof Double && right instanceof Double)
                    return (double)left + (double)right;
                if(left instanceof String && right instanceof String)
                    return (String) left + (String) right;
            case GREATER:
                checkNumberOperand(expr.operator,left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
            checkNumberOperand(expr.operator,left, right);
                return (double) left >= (double) right;
            case LESS:
            checkNumberOperand(expr.operator,left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
            checkNumberOperand(expr.operator,left, right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
            checkNumberOperand(expr.operator,left, right);
                return !isEqual(left,right);
            case EQUAL_EQUAL:
            checkNumberOperand(expr.operator,left, right);
                return isEqual(left,right); 
        }
        throw new RuntimeError(expr.operator, "operands must be 2 numbers or 2 strings");
    }

    void interpret(Expr expression){
        try{
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        }catch(RuntimeError err){
            lox.runtimeError(err);
        }
    }

    private boolean isEqual(Object a,Object b){
        if(a==null && b == null)return true;
        if(a==null)return false;
        return a.equals(b);
    }
    private void checkNumberOperand(Token operator,Object operand){
        if(operand instanceof Double)return;
        throw new RuntimeError(operator,"operand must be a number");
    }
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
    private void checkNumberOperand(Token operator,Object left,Object right){
        if(left instanceof Double && right instanceof Double) return;
    
        throw new RuntimeError(operator, "both of the operands must be numbers");
    }
    private String stringify(Object expr){
        if(expr == null ) return "nil";

        if(expr instanceof Double){
            String str = expr.toString();
            if(str.endsWith(".0")){
                return str.substring(0,str.length() -2);
            }
            return str;
        }
        return expr.toString();
    }

}
