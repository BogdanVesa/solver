package com.example.solver;

import org.mariuszgromada.math.mxparser.mathcollection.Evaluate;

import java.util.ArrayList;
import java.util.Arrays;

public class ZeroDegree implements Solve{
    ArrayList<EandQ> questions = new ArrayList<EandQ>();
    ArrayList<ExpressionTree> LandR = new ArrayList<ExpressionTree>();
    ArrayList<Node> den = new ArrayList<Node>();
    boolean fromDivision = false;

    String getEquation(){
        if(den.size()!=0){
            String denominator = "";
            String left;
            if(LandR.get(0).getExpression().contains("+") || LandR.get(0).getExpression().contains("-")){
                left = "("+LandR.get(0).getExpression()+")";
            }else{
                left = LandR.get(0).getExpression();
            }
            String right;
            if(LandR.get(1).getExpression().contains("+") || LandR.get(0).getExpression().contains("-")){
                right = "("+LandR.get(1).getExpression()+")";
            }else{
                right = LandR.get(1).getExpression();
            }
            denominator +=den.get(0).getData();
            for(int i=1;i< den.size();i++){
                denominator+="*"+den.get(i).getData();
            }
            return left+"/"+denominator+"="+right+"/"+denominator;
        }
        return LandR.get(0).getExpression()+"="+LandR.get(1).getExpression();
    }
    @Override
    public ArrayList<EandQ> Solve(String exp) {
        ArrayList<String> splitExp = new ArrayList<String>(Arrays.asList(exp.split("=")));
        ExpressionTree left;
        ExpressionTree right;

        if(isOnlyOneNumber(splitExp.get(0))) {
            Node n = new Node(splitExp.get(0));
            n.ContainsX();
            left = new ExpressionTree(n);
        } else{
            left = Convert(splitExp.get(0));
            left = NecessaryTransformations(left);
        }
        if(isOnlyOneNumber(splitExp.get(1))){
            Node n = new Node(splitExp.get(1));
            n.ContainsX();
            right = new ExpressionTree(n);
        } else{
            right = Convert(splitExp.get(1));
            right = NecessaryTransformations(right);
        }

        LandR.add(left);
        LandR.add(right);
        Question q1 = new Question();
        EandQ eq1 = new EandQ(getEquation(),q1);
        questions.add(eq1);

        solveMultiplication(LandR);
        solveDivision(LandR);
        if(den.size()>=1){
            String denominator = "";
            denominator +=den.get(0).getData();
            for(int i=1;i< den.size();i++){
                denominator+="*"+den.get(i).getData();
            }
            Question q4 = new Question("Care numitorul comun?",denominator);
            EandQ eq4 = new EandQ(getEquation(),q4);
            questions.add(eq4);
            den.clear();
        }
        solveMultiplication(LandR);
        OpenParantheses(LandR);
        solveMultiplication(LandR);

        double l = left.Evaluate(LandR.get(0).head);
        double r = right.Evaluate(LandR.get(1).head);
        String exp1="";
        if(isInt(l)){
            exp1+=Integer.toString((int)l);
            Question q2 = new Question("Care este rezultatul in stanga",Integer.toString((int)l ));
            EandQ eq2 = new EandQ(getEquation(),q1);
            questions.add(eq2);
        }else{
            exp1+=Double.toString(l);
            Question q3 = new Question("Care este rezultatul in stanga",Double.toString(l ));
            EandQ eq3 = new EandQ(getEquation(),q1);
            questions.add(eq3);
        }
        exp1+="=";
        if(isInt(r)){
            exp1+=Integer.toString((int)r);
            Question q5 = new Question("Care este rezultatul in dreapta",Integer.toString((int)r ));
            EandQ eq5 = new EandQ(getEquation(),q5);
            questions.add(eq5);
        }else{
            exp1+=Double.toString(r);
            Question q7 = new Question("Care este rezultatul in dreapta",Double.toString(r));
            EandQ eq7 = new EandQ(getEquation(),q1);
            questions.add(eq1);
        }
        Question q7 = new Question();
        EandQ eq7 = new EandQ(exp1,q7);
        questions.add(eq7);
        return questions;
    }

    boolean isInt(double n){
        return n%1==0;
    }
    boolean isOnlyOneNumber(String str) {
        return str.matches("-?[0-9]+x?");
    }

    void solveDivision(ArrayList<ExpressionTree> LandR){

        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);

        checkDivision(left, left.head, right);
        checkDivision(right, right.head, left);

        left.Inorder(left.head);
        System.out.println();
        right.Inorder(right.head);
        System.out.println();

    }

    private void checkDivision(ExpressionTree tree, Node node, ExpressionTree oppositeTree) {
        if (node == null) return;

        if(node.data.equals("/")){
            boolean solved = false;
            if(node.l != null && tree.op(node.l.data)==1){
                fromDivision = true;
                solveParentheses(node, tree);
            }
            if(node.r !=null && tree.op(node.r.data)==1){
                solveParentheses(node, tree);
                fromDivision = true;
            }
            if((node.l != null && node.r !=null)&&(node.l.isNumber() && node.r.isNumber())){
                double number = tree.Evaluate(node);
                if(number %1 == 0){
                    String equation = getEquation();
                    String exp = tree.getExp(node);
                    int n = (int) number;
                    node.data = Integer.toString(n);
                    node.r = null;
                    node.l =null;
                    solved = true;
                    Question q = new Question("Care este rezultatul impartiri "+exp+"?", node.data);
                    EandQ eq = new EandQ(equation,q);
                    questions.add(eq);
                }
            }
            if(!solved){
                Node temp = node.r;
                Node temp1 = node.l;
                tree.replaceNode(tree.head,node, node.l);
                Node n = new Node();
                n.copyNode(temp);
                den.add(n);
                commonDenominator(tree.head, temp, temp1, tree);
                commonDenominator(oppositeTree.head,temp,temp1, oppositeTree);
            }
        }
        checkDivision(tree, node.l, oppositeTree);
        checkDivision(tree, node.r, oppositeTree);
    }

    void commonDenominator(Node parent, Node denominator, Node node, ExpressionTree tree){
        if(parent == null) return;

        if(parent==node){
            return;
        }

        if(parent.parenthesis || tree.op(parent.data) == 2 || parent.isNumber()){
            Node newNode = new Node("*");
            Node copy = new Node();
            copy.copyNode(denominator);
            newNode.r = copy;
            newNode.l = parent;
            tree.replaceNode(tree.head, parent, newNode);
            return;
        }

        commonDenominator(parent.l, denominator,node, tree);
        commonDenominator(parent.r, denominator,node, tree);

    }

    void solveMultiplication(ArrayList<ExpressionTree> LandR){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);

        checkMultiplication(left,left.head);
        checkMultiplication(left,right.head);

        left.Inorder(left.head);
        System.out.println();
        right.Inorder(right.head);
        System.out.println();
    }
    void OpenParantheses(ArrayList<ExpressionTree> LandR){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);

        solveParentheses(left.head, left);
        solveParentheses(right.head, right);
        left.Inorder(left.head);
        System.out.println();
        right.Inorder(right.head);
        System.out.println();
    }

    private void solveParentheses(Node node, ExpressionTree tree) {
        if(node == null) return;

        solveParentheses(node.l, tree);
        solveParentheses(node.r, tree);
        if(node.data.equals("*")&& tree.subParenthesis(node)){
            solveParentheses(node, tree);
        }

        if(node.parenthesis){
            checkMultiplication(tree,node);
            String exp = tree.getExpression();
            String expPar = tree.getExp(node);
            String equation = getEquation();
            tree.solveParenthesis(node);
            Question q = new Question("Care este rezultatul parantezei "+expPar+" ?", tree.getExp(node));
            EandQ eq = new EandQ(equation,q);
            questions.add(eq);

        }
    }

    void checkMultiplication(ExpressionTree tree, Node parent){
        if(parent == null) return;
        checkMultiplication(tree, parent.l);
        checkMultiplication(tree, parent.r);

        if(parent.data.equals("*")){
            if(parent.l.isNumber()&& parent.r.isNumber()){
                double number = tree.Evaluate(parent);
                parent.data = Double.toString(number);
                parent.l = null;
                parent.r = null;
            }
        }
    }
    ExpressionTree Convert(String exp){
        ConvertToPostFix post;
        post = new ConvertToPostFix(exp);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(post.Convert().split(",")));
        ExpressionTree t = new ExpressionTree(list);
        t.ParenthesisCheck(t.head);
        return t;
    }

    ExpressionTree NecessaryTransformations(ExpressionTree tree){
        tree.ParenthesisCheck(tree.head);
        tree.ReplaceMinus(tree.head);
        return tree;
    }

}