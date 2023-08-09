package com.example.solver;


import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Stack;

public class ExpressionTree {
    Node head;
    String exp;
    String expression;

    public ExpressionTree(Node node){
        this.head= node;
    }
    public ExpressionTree(ArrayList<String> array){
        Stack<Node> st = new Stack<Node>();
        Node t1,t2,temp;

        for(String s : array){
            if(!isOp(s)){
                temp = new Node(s);
                st.push(temp);
            }
            else{
                temp = new Node(s);
                t1 = st.pop();
                t2 = st.pop();
                temp.l = t2;
                temp.r = t1;
                st.push(temp);
            }
        }
        temp = st.pop();
        this.head = temp;
        NodesContainsX(this.head);
    }
    void setHead(Node node){
        this.head =node;
    }
    private void NodesContainsX(Node node){
        if (node == null) return;
        node.ContainsX();
        NodesContainsX(node.l);
        NodesContainsX(node.r);
    }

    String getExpression(){
        this.expression = "";
        expressionInorder(this.head);
        return this.expression;
    }
    public void expressionInorder(Node parent){
        if ( parent == null ) return ;

        if(parent.parenthesis){
            this.expression +="(";
            expressionInorder(parent.l);
            if(!isNegativeNumber(parent))
                this.expression +=parent.getData();
            expressionInorder(parent.r);
            this.expression +=")";
        }
        else{
            expressionInorder(parent.l);
            if(!isNegativeNumber(parent))
                this.expression +=parent.getData();
            expressionInorder(parent.r);
        }
    }

    void ParenthesisCheck(Node node){
        if ( node == null ) return;

        if(isOp(node.data)){
            int preValue = op(node.data); // precedence value
            if(isOp(node.l.data)){
                int value = op(node.l.data);
                if(preValue > value){
                    node.l.parenthesis = true;
                }
            }
            if(isOp(node.r.data)){
                int value = op(node.r.data);
                if(preValue > value){
                    node.r.parenthesis = true;
                }
                if(preValue == value && preValue == 2 ){
                    node.r.parenthesis = true;
                }
            }
        }
        if(op(node.data)==1){
            if(op(node.r.data)==1){
                node.r.parenthesis = true;
            }
        }
        ParenthesisCheck(node.l);
        ParenthesisCheck(node.r);
    }
    public void Inorder(Node parent){
        if ( parent == null ) return ;

        if(parent.parenthesis){
            System.out.print("(");
            Inorder(parent.l);
            if(!isNegativeNumber(parent))
                System.out.print(parent.getData());
            Inorder(parent.r);
            System.out.print(")");
        }
        else{
            Inorder(parent.l);
            if(!isNegativeNumber(parent))
                System.out.print(parent.getData());
            Inorder(parent.r);
        }
    }

    public String getNumberExp(Node node){
        this.exp = "";
        numberExp(node);
        return exp;
    }
    void numberExp(Node node){
        if ( node == null ) return ;
        numberExp(node.l);
        if(!isNegativeNumber(node))
            this.exp +=node.data;
        numberExp(node.r);
    }


    public String getExp(Node node){
        this.exp="";
        expInorder(node);
        return this.exp;
    }
    void expInorder(Node node){
        if ( node == null ) return ;
        expInorder(node.l);
        if(!isNegativeNumber(node))
            this.exp +=node.getData();
        expInorder(node.r);
    }

    public void CleanTree(Node node){
        if (node == null) return;

        if(node.l!= null)
            CleanTree(node.l);
        if(node.l!= null)
            CleanTree(node.r);

        if(node.l != null && op(node.l.data) == 1){
            boolean nullCase = true;
            if(node.l.r == null && node.l.l != null && node.l.l.data.matches("-?[0-9]+")) {
                node.l = node.l.l;
                nullCase = false;
            }
            if(node.l.l == null && node.l.r != null && node.l.r.data.matches("-?[0-9]+")) {
                node.l = node.l.r;
                nullCase = false;
            }
            if(node.l.l != null && op(node.l.l.data) == 1 && node.l.r == null){
                node.l = node.l.l;
                nullCase = false;
            }
            if(node.l.l == null && node.l.r == null && nullCase) {
                node.l = null;
            }
        }
        if(node == this.head && !node.isNumber()){
            if(node.r == null && node.l == null ){
                node = new Node("0");
                this.head = node;
            }
            if(node.r == null && node.l != null ){
                node = node.l;
                this.head = node;
            }
            if(node.l == null && node.r != null ){
                node = node.r;
                this.head = node;
            }
        }
    }

    //
    public void solveParenthesis(Node node){
        if(isContainXSqr(node)){
            ArrayList<Node> nodesXSqr = getNumbersXSqr(node);
            ArrayList<Node> nodesX = getNumbers(node, true);
            ArrayList<Node> nodes = getNumbers(node, false);
            String expXSqr = "";
            expXSqr += nodesXSqr.get(0).data;
            for (int i = 1; i < nodesXSqr.size(); i++) {
                if (nodesXSqr.get(i).data.contains("-")) {
                    expXSqr = expXSqr + nodesXSqr.get(i).data;
                } else {
                    expXSqr = expXSqr + "+" + nodesXSqr.get(i).data;
                }
            }
            String expX = "";
            if(nodesX.size()>0) {
                expX += nodesX.get(0).data;
                for (int i = 1; i < nodesX.size(); i++) {
                    if (nodesX.get(i).data.contains("-")) {
                        expX = expX + nodesX.get(i).data;
                    } else {
                        expX = expX + "+" + nodesX.get(i).data;
                    }
                }
            }
            String exp = "";
            if(nodes.size()>0) {
                exp += nodes.get(0).data;
                for (int i = 1; i < nodes.size(); i++) {
                    if (nodes.get(i).data.contains("-")) {
                        exp = exp + nodes.get(i).data;
                    } else {
                        exp = exp + "+" + nodes.get(i).data;
                    }
                }
            }
            Expression exp3 = new Expression(expXSqr);
            double xSqrFactor = exp3.calculate();
            Node a = new Node(Double.toString(xSqrFactor));
            Node b = null;
            Node plus = null;
            if(!expX.equals("")) {
                Expression exp1 = new Expression(expX);
                double xFactor = exp1.calculate();
                b = new Node(Double.toString(xFactor));
                b.x = true;
                plus = new Node("+");
                plus.l = a;
                plus.r = b;
            }
            Node c = null;
            if(!exp.equals("")) {
                Expression exp2 = new Expression(exp);
                double constant = exp2.calculate();
                c =  new Node(Double.toString(constant));
            }
            a.xSqr = true;
            if(plus !=null && c!=null){
                node.l = plus;
                node.r = c;
            }else if(plus ==null && c!=null){
                node.l = a;
                node.r = c;
            }else if(plus !=null){
                node.l =a;
                node.r =b;
            } else{
                node.data = a.data;
                node.xSqr = true;
                node.parenthesis = false;
            }
        }else if(isContainX(node)){
            ArrayList<Node> nodesX = getNumbers(node, true);
            ArrayList<Node> nodes = getNumbers(node, false);
            String expX = "";
            expX += nodesX.get(0).data;
            for (int i = 1; i < nodesX.size(); i++) {
                if (nodesX.get(i).data.contains("-")) {
                    expX = expX + nodesX.get(i).data;
                } else {
                    expX = expX + "+" + nodesX.get(i).data;
                }
            }
            String exp = "0";
            if(nodes.size()>0) {
                exp += nodes.get(0).data;
                for (int i = 1; i < nodes.size(); i++) {
                    if (nodes.get(i).data.contains("-")) {
                        exp = exp + nodes.get(i).data;
                    } else {
                        exp = exp + "+" + nodes.get(i).data;
                    }
                }
            }

            Expression exp1 = new Expression(expX);
            double xFactor = exp1.calculate();
            Node left = new Node(Double.toString(xFactor));
            left.x = true;
            Node right = null;
            if(!exp.equals("")) {
                Expression exp2 = new Expression(exp);
                double constant = exp2.calculate();
                right = new Node(Double.toString(constant));
            }
            if(right != null){
                node.l = left;
                node.r = right;
            } else {
                node.data = left.data;
                node.x = true;
                node.parenthesis = false;
            }
        } else {
            double number = Evaluate(node);
            node.data = Double.toString(number);
            node.l = null;
            node.r = null;
            node.parenthesis = false;
        }
    }

    public void balanceTree(Node parent, Node node){
        if(parent == null) return;

        if(parent.r != null && parent.r.equals(node)){
            if(parent.data.equals("+")){
                Node temp = parent.l;
                Node temp2 = node.l;
                parent.r = node.r;
                parent.l = node;
                node.r = temp2;
                node.l = temp;
                node.parenthesis = false;
            }
            if(parent.data.equals("-")){
                String expression;
                expression = "-1*"+node.l.data;
                Expression left = new Expression(expression);
                node.l.data = Double.toString(left.calculate());
                expression = "-1*"+node.r.data;
                Expression right = new Expression(expression);
                node.r.data = Double.toString(right.calculate());
                parent.data = "+";
                Node temp = parent.l;
                Node temp2 = node.l;
                parent.r = node.r;
                parent.l = node;
                node.r = temp2;
                node.l = temp;
                node.parenthesis = false;
            }
            if(parent.data.equals("*") && parent.l.isNumber()){
                Node grandparent = getParent(parent, this.head);
                String exp;
                if(grandparent.data.equals("-")){
                    exp = "-1*"+parent.l.data;
                    grandparent.data = "+";
                }else{
                    exp = parent.l.data;
                }
                String exp1 = exp+"*"+node.l.data;
                Expression left = new Expression(exp1);
                node.l.data = Double.toString(left.calculate());
                String exp2 = exp+"*"+node.r.data;
                Expression right = new Expression(exp2);
                node.r.data = Double.toString(right.calculate());
                replaceNode(this.head, parent, node);
                fixTree(this.head, node);
                node.parenthesis = false;
                ParenthesisCheck(this.head);
            }
        }
        if(parent.l != null && parent.l.equals(node)){
            if(parent.data.equals("*") && parent.r != null && parent.r.isNumber()){
                Node grandparent = getParent(parent, this.head);
                String exp;
                if(grandparent.data.equals("-")){
                    exp = "-1*"+parent.r.data;
                    grandparent.data = "+";
                }else{
                    exp = parent.r.data;
                }
                String exp1 = exp+"*"+node.l.data;
                Expression left = new Expression(exp1);
                node.l.data = Double.toString(left.calculate());
                String exp2 = exp+"*"+node.r.data;
                Expression right = new Expression(exp2);
                node.r.data = Double.toString(right.calculate());
                replaceNode(this.head, parent, node);
                fixTree(this.head, node);
                node.parenthesis = false;
                ParenthesisCheck(this.head);
            }
        }
        balanceTree(parent.l,node);
        balanceTree(parent.r,node);
    }
    Node getParent(Node son , Node parent){
        Node result = null;
        if(this.head==(son)){
            Node plus = new Node("+");
            plus.r = son;
            plus.l = null;
            this.setHead(plus);
            result = plus;
            return result;
        }
        if(parent.l != null && parent.l==(son)) {
            return parent;
        }
        if(parent.r != null && parent.r==(son)) {
            return parent;
        }

        if(result== null && parent.l != null) result = getParent(son,parent.l);
        if(result== null && parent.r != null) result = getParent(son,parent.r);
        return result;

    }

    void balanceTreeXSqr(Node parent, Node node){
        if(parent == null) return;
        if(parent.r != null && parent.r==node){
            if(parent.data.equals("+")){
                boolean containXSqr = isContainXSqr(node);
                boolean containX = isContainX(node);
                boolean containNumber = isContainNumber(node);

                if(containXSqr && containX && containNumber){
                    Node temp1 = parent.l;
                    parent.r = node.r;
                    parent.l = node;
                    node.r = node.l.r;
                    node.l.r =node.l.l;
                    node.l.l = temp1;
                    node.parenthesis = false;
                }else if(containXSqr && containX){
                    fix(parent,node);
                }else if(containXSqr && containNumber){
                    fix(parent,node);
                } else if(containX && containNumber){
                    fix(parent,node);
                }
            }
            if(parent.data.equals("-")){
                boolean containXSqr = isContainXSqr(node);
                boolean containX = isContainX(node);
                boolean containNumber = isContainNumber(node);
                String expression;
                if(containXSqr && containX && containNumber) {
                    expression = "-1*"+node.r.data;
                    Expression c = new Expression(expression);
                    node.r.data = Double.toString(c.calculate());
                    expression = "-1*"+node.l.r.data;
                    Expression b = new Expression(expression);
                    node.l.r.data = Double.toString(b.calculate());
                    expression = "-1*"+node.l.l.data;
                    Expression a = new Expression(expression);
                    node.l.l.data = Double.toString(a.calculate());
                    Node temp1 = parent.l;
                    parent.r = node.r;
                    parent.l = node;
                    node.r = node.l.r;
                    node.l.r = node.l.l;
                    node.l.l = temp1;
                    node.parenthesis = false;
                }else if(containXSqr && containX){
                    expression = "-1*"+node.l.data;
                    Expression a = new Expression(expression);
                    node.l.data = Double.toString(a.calculate());
                    expression = "-1*"+node.r.data;
                    Expression b = new Expression(expression);
                    node.r.data = Double.toString(b.calculate());
                    fix(parent,node);
                }else if(containXSqr && containNumber){
                    expression = "-1*"+node.l.data;
                    Expression a = new Expression(expression);
                    node.l.data = Double.toString(a.calculate());
                    expression = "-1*"+node.r.data;
                    Expression c = new Expression(expression);
                    node.r.data = Double.toString(c.calculate());
                    fix(parent,node);
                } else if(containX && containNumber){
                    expression = "-1*"+node.l.data;
                    Expression b = new Expression(expression);
                    node.l.data = Double.toString(b.calculate());
                    expression = "-1*"+node.r.data;
                    Expression c = new Expression(expression);
                    node.r.data = Double.toString(c.calculate());
                    fix(parent,node);
                }
            }
            if(parent.data.equals("*")){
                if(parent.l!=null && parent.l.isNumber()){
                    boolean containXSqr = isContainXSqr(node);
                    boolean containX = isContainX(node);
                    boolean containNumber = isContainNumber(node);
                    Node grandparent = getParent(parent, this.head);
                    String exp;
                    if(grandparent.data.equals("-")){
                        exp = "-1*"+parent.l.data;
                        grandparent.data = "+";
                    }else{
                        exp = parent.l.data;
                    }
                    if(containXSqr && containX && containNumber){
                        String exp1 = exp+"*"+node.l.l.data;
                        Expression a = new Expression(exp1);
                        node.l.l.data = Double.toString(a.calculate());
                        String exp2 = exp+"*"+node.l.r.data;
                        Expression b = new Expression(exp2);
                        node.l.r.data = Double.toString(b.calculate());
                        String exp3 = exp+"*"+node.r.data;
                        Expression c = new Expression(exp3);
                        node.r.data = Double.toString(c.calculate());
                        replaceNode(this.head, parent, node);
                        Node temp1 = grandparent.l;
                        grandparent.r = node.r;
                        grandparent.l = node;
                        node.r = node.l.r;
                        node.l.r =node.l.l;
                        node.l.l = temp1;
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    }else if(containXSqr && containX){
                        String exp1 = exp+"*"+node.l.data;
                        Expression a = new Expression(exp1);
                        node.l.data = Double.toString(a.calculate());
                        String exp2 = exp+"*"+node.r.data;
                        Expression b = new Expression(exp2);
                        node.r.data = Double.toString(b.calculate());
                        replaceNode(this.head, parent, node);
                        fixTree(this.head, node);
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    }else if(containXSqr && containNumber){
                        String exp1 = exp+"*"+node.l.data;
                        Expression a = new Expression(exp1);
                        node.l.data = Double.toString(a.calculate());
                        String exp2 = exp+"*"+node.r.data;
                        Expression c = new Expression(exp2);
                        node.r.data = Double.toString(c.calculate());
                        replaceNode(this.head, parent, node);
                        fixTree(this.head, node);
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    } else if(containX && containNumber){
                        String exp1 = exp+"*"+node.l.data;
                        Expression b = new Expression(exp1);
                        node.l.data = Double.toString(b.calculate());
                        String exp2 = exp+"*"+node.r.data;
                        Expression c = new Expression(exp2);
                        node.r.data = Double.toString(c.calculate());
                        if(parent.l.x){
                            node.l.x =false;
                            node.l.xSqr = true;
                            node.r.x = true;
                        }
                        replaceNode(this.head, parent, node);
                        fixTree(this.head, node);
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    }
                }
                if(parent.l != null && parent.l.parenthesis){
                    Node left = parent.l;
                    Node right = parent.r;

                    String exp;
                    Node grandparent = getParent(parent, this.head);
                    if(grandparent.data.equals("-")){
                        exp = "-1*";
                        grandparent.data = "+";
                    }else{
                        exp = "1*";
                    }
                    Node leftX = findNumber(left, true);
                    Node leftC = findNumber(left, false);
                    Node rightX = findNumber(right, true);
                    Node rightC = findNumber(right, false);
                    String exp1 = exp+ leftX.data+"*"+rightX.data;
                    Expression a = new Expression(exp1);
                    String exp2 = exp+"("+leftX.data+"*"+rightC.data+"+"+rightX.data+"*"+leftC.data+")";
                    Expression b = new Expression(exp2);
                    String exp3 = exp+ leftC.data+"*"+rightC.data;
                    Expression c = new Expression(exp3);
                    Node newNode = new Node("+");
                    newNode.r = new Node(Double.toString(c.calculate()));
                    Node plus = new Node("+");
                    plus.l = new Node(Double.toString(a.calculate()));
                    plus.l.xSqr = true;
                    plus.r = new Node(Double.toString(b.calculate()));
                    plus.r.x = true;
                    newNode.l = plus;
                    Node temp1 = grandparent.l;
                    grandparent.r = newNode.r;
                    grandparent.l = newNode;
                    newNode.r = newNode.l.r;
                    newNode.l.r =newNode.l.l;
                    newNode.l.l = temp1;
                    newNode.parenthesis = false;
                    ParenthesisCheck(this.head);
                }
            }
        }
        if(parent.l != null && parent.l==(node)){
            if(parent.data.equals("*")) {
                if(parent.r!=null && parent.r.isNumber()){
                    boolean containXSqr = isContainXSqr(node);
                    boolean containX = isContainX(node);
                    boolean containNumber = isContainNumber(node);
                    Node grandparent = getParent(parent, this.head);
                    String exp;
                    if (grandparent.data.equals("-")) {
                        exp = "-1*" + parent.r.data;
                        grandparent.data = "+";
                    } else {
                        exp = parent.r.data;
                    }
                    if (containXSqr && containX && containNumber) {
                        String exp1 = exp + "*" + node.l.l.data;
                        Expression a = new Expression(exp1);
                        node.l.l.data = Double.toString(a.calculate());
                        String exp2 = exp + "*" + node.l.r.data;
                        Expression b = new Expression(exp2);
                        node.l.r.data = Double.toString(b.calculate());
                        String exp3 = exp + "*" + node.r.data;
                        Expression c = new Expression(exp3);
                        node.r.data = Double.toString(c.calculate());
                        replaceNode(this.head, parent, node);
                        Node temp1 = grandparent.l;
                        grandparent.r = node.r;
                        grandparent.l = node;
                        node.r = node.l.r;
                        node.l.r = node.l.l;
                        node.l.l = temp1;
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    } else if (containXSqr && containX) {
                        String exp1 = exp + "*" + node.l.data;
                        Expression a = new Expression(exp1);
                        node.l.data = Double.toString(a.calculate());
                        String exp2 = exp + "*" + node.r.data;
                        Expression b = new Expression(exp2);
                        node.r.data = Double.toString(b.calculate());
                        replaceNode(this.head, parent, node);
                        fixTree(this.head, node);
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    } else if (containXSqr && containNumber) {
                        String exp1 = exp + "*" + node.l.data;
                        Expression a = new Expression(exp1);
                        node.l.data = Double.toString(a.calculate());
                        String exp2 = exp + "*" + node.r.data;
                        Expression c = new Expression(exp2);
                        node.r.data = Double.toString(c.calculate());
                        replaceNode(this.head, parent, node);
                        fixTree(this.head, node);
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    } else if (containX && containNumber) {
                        String exp1 = exp + "*" + node.l.data;
                        Expression b = new Expression(exp1);
                        node.l.data = Double.toString(b.calculate());
                        String exp2 = exp + "*" + node.r.data;
                        Expression c = new Expression(exp2);
                        node.r.data = Double.toString(c.calculate());
                        if(parent.l.x){
                            node.l.x =false;
                            node.l.xSqr = true;
                            node.r.x = true;
                        }
                        replaceNode(this.head, parent, node);
                        fixTree(this.head, node);
                        node.parenthesis = false;
                        ParenthesisCheck(this.head);
                    }
                }
            }
            if(parent.data.equals("^")){
                if(parent.r != null && parent.r.data.equals("2")){
                    Node grandparent = getParent(parent, this.head);
                    String exp;
                    if (grandparent.data.equals("-")) {
                        exp = "-1*";
                        grandparent.data = "+";
                    } else {
                        exp = "1*";
                    }
                    String exp1 = exp+node.l.data+"*"+node.l.data;
                    String exp2 = exp+"("+"2*"+node.l.data+"*"+node.r.data+")";
                    String exp3 = exp+node.r.data+"*"+node.r.data;
                    Expression a = new Expression(exp1);
                    Expression b = new Expression(exp2);
                    Expression c = new Expression(exp3);
                    Node newNode = new Node("+");
                    newNode.r = new Node(Double.toString(c.calculate()));
                    Node plus = new Node("+");
                    plus.l = new Node(Double.toString(a.calculate()));
                    plus.l.xSqr = true;
                    plus.r = new Node(Double.toString(b.calculate()));
                    plus.r.x = true;
                    newNode.l = plus;
                    Node temp1 = grandparent.l;
                    grandparent.r = newNode.r;
                    grandparent.l = newNode;
                    newNode.r = newNode.l.r;
                    newNode.l.r =newNode.l.l;
                    newNode.l.l = temp1;
                    newNode.parenthesis = false;
                    ParenthesisCheck(this.head);

                }
            }
        }
        balanceTreeXSqr(parent.l,node);
        balanceTreeXSqr(parent.r,node);
    }
    void fix(Node parent, Node node){
        Node temp = parent.l;
        Node temp2 = node.l;
        parent.r = node.r;
        parent.l = node;
        node.r = temp2;
        node.l = temp;
        node.parenthesis = false;
    }
    void replaceNode(Node parent, Node node, Node newNode){
        if(parent == null) return;
        if(this.head == node){
            this.setHead(newNode);
            return;
        }
        if(this.head.r != null && this.head.r==node && this.head.l == null){
            this.setHead(newNode);
            return;
        }
        if(parent.l != null && parent.l == node){
            parent.l = newNode;
            return;
        }
        if(parent.r != null && parent.r ==node){
            parent.r = newNode;
            return;
        }
        replaceNode(parent.l,node,newNode);
        replaceNode(parent.r,node,newNode);
    }
    void fixTree(Node parent, Node node){
        if(parent == null) return;
        fixTree(parent.l, node);
        fixTree(parent.r, node);

        if(parent.r != null && parent.r==node){
            Node temp = parent.l;
            Node temp2 = node.l;
            parent.r = node.r;
            parent.l = node;
            node.r = temp2;
            node.l = temp;
            node.parenthesis = false;
        }

    }
    boolean subParenthesis(Node node){
        if(node == null) return false;
        boolean result = false;
        if(node.parenthesis){
            return true;
        }
        else{
            if(subParenthesis(node.l)){
                result = true;
            }
            if(subParenthesis(node.r)){
                result = true;
            }
        }

        return result;
    }
    public ArrayList<Node> getNumbers(Node node, boolean x){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = findNumber(node, x);
        while(number != null){
            result.add(number);
            number = findNumber(node, x);
        }
        return result;
    }
    public Node findNumber(Node node, boolean x){
        Node result = null;
        if(this.head.isNumber() && !this.head.data.equals("0") && this.head.x ==x){
            result = this.head;
            Node newHead = new Node("0");
            this.setHead(newHead);
            return result;
        }
        if(node.l != null) {
            if (node.l.isNumber() && node.l.x == x) {
                result = node.l;
                node.l = null;
                return result;
            }
        }
        if(node.r != null) {
            if (node.r.isNumber() && node.r.x == x) {
                result = node.r;
                node.r = null;
                return result;
            }
        }
        if(result == null && node.l != null){
            result = findNumber(node.l,x);
        }
        if(result == null && node.r != null){
            result = findNumber(node.r,x);
        }
        return result;
    }
    ArrayList<Node> getNumbersXSqr(Node node){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = findNumberXSqr(node);
        while(number != null){
            result.add(number);
            number = findNumberXSqr(node);
        }
        return result;
    }
    Node findNumberXSqr(Node node){
        Node result = null;
        if(this.head.isNumber() && !this.head.data.equals("0") && this.head.xSqr){
            result = this.head;
            Node newHead = new Node("0");
            this.setHead(newHead);
            return result;
        }
        if(node.l != null) {
            if (node.l.isNumber() && node.l.xSqr) {
                result = node.l;
                node.l = null;
                return result;
            }
        }
        if(node.r != null) {
            if (node.r.isNumber() && node.r.xSqr) {
                result = node.r;
                node.r = null;
                return result;
            }
        }
        if(result == null && node.l != null){
            result = findNumberXSqr(node.l);
        }
        if(result == null && node.r != null){
            result = findNumberXSqr(node.r);
        }
        return result;
    }

    public double Evaluate(Node node){
        double result = 0;
        String expression = getNumberExp(node);
        Expression exp2 = new Expression(expression);
        result =  exp2.calculate();
        return result;
    }

    public boolean isContainX(Node node){
        if(node == null) return false;
        boolean result = false;
        if(node.x){
            return true;
        }
        else{
            if(isContainX(node.l)){
                result = true;
            }
            if(isContainX(node.r)){
                result = true;
            }
        }

        return result;
    }

    public boolean isContainXSqr(Node node){
        if(node == null) return false;
        boolean result = false;
        if(node.xSqr){
            return true;
        }
        else{
            if(isContainXSqr(node.l)){
                result = true;
            }
            if(isContainXSqr(node.r)){
                result = true;
            }
        }

        return result;
    }
    public boolean isContainNumber(Node node){
        if(node == null) return false;
        boolean result = false;
        if(node.isNumber()&& !node.x && !node.xSqr){
            return true;
        }
        else{
            if(isContainNumber(node.l)){
                result = true;
            }
            if(isContainNumber(node.r)){
                result = true;
            }
        }

        return result;
    }
    private boolean isNegativeNumber(Node node){
        if(node.r == null) return false;
        if(node.r.data.matches("-[0-9]+\\.?[0-9]*")){
            return true;
        }
        return false;
    }


    public void ReplaceMinus(Node node){
        if(node == null) return;

        if(node.data.equals("-") && !isOp(node.r.data)){
            node.data = "+";
            node.r.data = "-"+node.r.data;
        }
        ReplaceMinus(node.l);
        ReplaceMinus(node.r);
    }
    public void ReplaceMultiplication(Node node){
        if(node == null) return;

        ReplaceMultiplication(node.l);
        ReplaceMultiplication(node.r);
        if(node.l !=null && node.r!=null && node.l.x && node.r.x){
            return;
        }

        if(node.data.equals("*")){
            if((node.l.x || node.r.x) && (node.l.isNumber() && node.r.isNumber())){
                int number = (int) Evaluate(node);
                node.data = Integer.toString(number);
                node.x = true;
                node.l =null;
                node.r = null;
            }
        }
    }
    public void ReplacePower(Node node){
        if(node == null) return;

        if(node.data.equals("^")){
            if(node.l.x && node.r.data.equals("2")){
                node.data = node.l.data;
                node.x = false;
                node.xSqr = true;
                node.l = null;
                node.r= null;
            }
        }
        ReplacePower(node.l);
        ReplacePower(node.r);
    }
    public boolean isOp(String ch){
        String op = "+-*/^";
        if(op.contains(ch)){
            return true;
        }
        return false;
    }
    public int op(String c){
        if(c.equals("+") || c.equals("-")){
            return 1;
        }
        if(c.equals("*")|| c.equals("/")){
            return 2;
        }
        if(c.equals("^")){
            return 3;
        }
        return -1;
    }
}