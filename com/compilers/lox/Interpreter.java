package com.compilers.lox;

import java.util.List;

class Interpreter implements Expr.Visitor<Object>,
                             Stmt.Visitor<Void> {


    private Environment environment = new Environment();

    void interpret(List<Stmt> statements){
        try{
            for(Stmt statement: statements){
                execute(statement);
            }
        }catch(RuntimeError err){
            lox.runtimeError(err);
        }
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt){
        evaluate(stmt.expression);
        return null;
    }
    @Override
    public Void visitPrintStmt(Stmt.Print stmt){
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }                            
    @Override
    public Void visitVarStmt(Stmt.Var stmt){
        Object value = null;
        if(stmt.initializer!=null){
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }
    @Override
    public Void visitBlockStmt(Stmt.Block stmt){
        executeBlock(stmt.statements,new Environment(environment));
        return null;
    }
    @Override
    public Void visitIfStmt(Stmt.If stmt){
        if(isTruthy(evaluate(stmt.condition))){
            execute(stmt.thenBranch);
        }else{
            execute(stmt.elseBranch);
        }
        return null;
    }
    @Override
    public Void visitWhileStmt(Stmt.While stmt){
        while(isTruthy(evaluate(stmt.condition))){
            execute(stmt.body);
        }
        return null;
    }

    @Override 
    public Object visitLogicalExpr(Expr.Logical expr){
        Object left = evaluate(expr.left);
        if(expr.operator.type == TokenType.OR){
            if(isTruthy(left)) return left;
        }else{
            if(!isTruthy(left)) return left;
        }
        return evaluate(expr.right);
    }
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
    @Override 
    public Object visitVariableExpr(Expr.Variable expr){
        return environment.get(expr.name);
    }
    @Override 
    public Object visitAssignExpr(Expr.Assign expr){
        Object value = evaluate(expr.value);
        environment.assign(expr.name,value);
        return value;
    }
    
    
    void executeBlock(List<Stmt> statements, Environment environment){
        Environment previous = this.environment;
        try{
            this.environment = environment;
            for(Stmt statement : statements){
                execute(statement);
            }
        } finally{
            this.environment = previous;
        }
    }
    
    private void execute(Stmt stmt){
        stmt.accept(this); 
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
