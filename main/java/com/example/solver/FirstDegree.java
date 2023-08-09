package com.example.solver;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstDegree implements Solve{
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

        Question q19 = new Question();
        EandQ eq19 = new EandQ(getEquation(),q19);
        questions.add(eq19);

        solveMultiplication(LandR);
        if(isHigherDegree(LandR)){
            callRightDegree();
            return questions;
        }

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

        if(isLowerDegree(LandR)){
            callRightDegree();
            return questions;
        }

        String before = getEquation();
        solveMultiplication(LandR);
        String after = getEquation();
        if(!after.equals(before)){
            EandQ eq9 = new EandQ(getEquation(),new Question());
            questions.add(eq9);
        }

        fromDivision = false;
        LandR = OpenParentheses(LandR);

        if(isHigherDegree(LandR)){
            callRightDegree();
            return questions;
        }
        before = getEquation();
        solveMultiplication(LandR);
        if(isHigherDegree(LandR)){
            callRightDegree();
            return questions;
        }

        after = getEquation();
        if(!after.equals(before)){
            EandQ eq10 = new EandQ(getEquation(),new Question());
            questions.add(eq10);
        }

        LandR = MoveNumbers(LandR);
        int l = Calculate(LandR.get(0),LandR.get(0).head);
        int r = Calculate(LandR.get(1),LandR.get(1).head);
        String equation = getEquation();
        Question q = new Question("Care este rezultatul in partea stangă?", Integer.toString(l));
        EandQ eq = new EandQ(equation,q);
        questions.add(eq);
        String newEq="";
        if(l==1){
            newEq +="x";
        }else{
             newEq+= l+"x";
        }
        newEq +="="+LandR.get(1).getExpression();
        q = new Question("Care este rezultatul in partea dreaptă?", Integer.toString(r));
        eq = new EandQ(newEq,q);
        questions.add(eq);
        String fEq = l+"x"+"="+r;
        if(r==0){
            EandQ eq5 = new EandQ("x=0", new Question());
            questions.add(eq5);
        }
        if(l!=1){
            String result = getResult(l,r);
            Question q1 = new Question("Care este x?", result);
            EandQ eq1 = new EandQ(fEq,q1);
            questions.add(eq1);
            EandQ eq2 = new EandQ("x="+result, new Question());
            questions.add(eq2);
        }else{
            String result = "x="+r;
            EandQ eq3 = new EandQ(result, new Question());
            questions.add(eq3);
        }
        return questions;
    }

    boolean isHigherDegree(ArrayList<ExpressionTree> LandR){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);
        if(left.isContainXSqr(left.head)){
            return true;
        }
        if(right.isContainXSqr(right.head)){
            return true;
        }
        return false;
    }
    boolean isLowerDegree(ArrayList<ExpressionTree> LandR){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);
        if(!left.isContainXSqr(left.head) && !left.isContainX(left.head)){
            return true;
        }
        if(!right.isContainXSqr(left.head) && !right.isContainX(left.head)){
            return true;
        }
        return false;
    }
    String getResult(int l, int r){
        String result;
        double res = (double)r/l;
        if(res % 1!=0){
            result = r +"/"+ l;
            return result;
        }
        int x = (int) res;
        result = Integer.toString(x);
        return result;
    }
    int Calculate(ExpressionTree tree, Node node){
        int result = 0;
        String expression = tree.getNumberExp(node);
        Expression exp2 = new Expression(expression);
        result = (int)exp2.calculate();
        return result;
    }

    void callRightDegree(){
        Equation eq = new Equation(getEquation());
        eq.setType();
        ArrayList<EandQ> q = eq.solve();

        questions.addAll(q);
    }
    ArrayList<ExpressionTree> MoveNumbers(ArrayList<ExpressionTree> LandR){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);
        String equation = getEquation();
        ArrayList<Node> addNumbersToRight = LeftNumbers(left);

        ArrayList<Node> addNumbersToLeft = RightNumbers(right);

        String numbers = getStringNumbers(addNumbersToRight);
        if(!numbers.equals("")) {
            Question q = new Question("Care sunt numerele in partea stanga ce trebuie mutate in dreapta?", numbers);
            EandQ eq = new EandQ(equation, q);
            questions.add(eq);
        }
        numbers = getStringNumbers(addNumbersToLeft);
        if(!numbers.equals("")){
            Question q = new Question("Care sunt numerele in partea dreapta ce trebuie mutate in stanga?", numbers);
            EandQ eq = new EandQ(equation, q);
            questions.add(eq);
        }

        left.CleanTree(left.head);

        right.CleanTree(right.head);

        left = AddNumbers(left, addNumbersToLeft);

        right = AddNumbers(right, addNumbersToRight);

        Question q1 = new Question("Cum arata ecuatia dupa mutarea numerelor", getEquation());
        EandQ eq1 = new EandQ(equation,q1);
        questions.add(eq1);

        ArrayList<ExpressionTree> result = new ArrayList<ExpressionTree>();
        result.add(left);
        result.add(right);

        return result;
    }
    String getStringNumbers(ArrayList<Node> numbers){
        String result="";
        if(numbers.size()==0){
            return result;
        }
        result += numbers.get(0).getData();
        for(int i=1;i< numbers.size();i++){
            result+=", "+numbers.get(i).getData();
        }
        return result;
    }

    ArrayList<Node> LeftNumbers(ExpressionTree left){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = findNumber(left.head, false, left);
        while(number != null){
            result.add(number);
            number = findNumber(left.head, false, left);
        }
        return result;
    }
    ArrayList<Node> RightNumbers(ExpressionTree right){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = findNumber(right.head, true,right);
        while(number != null){
            result.add(number);
            number = findNumber(right.head, true,right);
        }
        return result;
    }

    Node findNumber(Node node, Boolean x, ExpressionTree tree){
        Node result = null;
        if(tree.head.isNumber() && !tree.head.data.equals("0") && tree.head.x == x){
            result = tree.head;
            Node newHead = new Node("0");
            tree.setHead(newHead);
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
            result = findNumber(node.l,x,tree);
        }
        if(result == null && node.r != null){
            result = findNumber(node.r,x,tree);
        }
        return result;
    }

    ExpressionTree AddNumbers(ExpressionTree tree, ArrayList<Node> nodes){
        Node plus;
        for(Node i: nodes){
            if(tree.head.data.equals("0")){
                if(i.data.contains("-")){
                    i.data = i.data.replace("-","");
                }
                else{
                    i.data ="-"+i.data;
                }
                tree.setHead(i);
                continue;
            }
            plus = new Node("+");
            if(i.data.contains("-")){
                i.data = i.data.replace("-","");
            }
            else{
                i.data ="-"+i.data;
            }
            plus.r = i;
            plus.l = tree.head;
            tree.setHead(plus);
        }
        return tree;
    }
    ArrayList<ExpressionTree> OpenParentheses(ArrayList<ExpressionTree> trees){
        ExpressionTree left = trees.get(0);
        ExpressionTree right = trees.get(1);

        solveParentheses(left.head, left);
        solveParentheses(right.head, right);


        ArrayList<ExpressionTree> result = new ArrayList<ExpressionTree>();
        result.add(left);
        result.add(right);
        return result;
    }
    void solveParentheses(Node node, ExpressionTree tree){
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
            boolean sameTerms = false;
            if(!node.parenthesis){
                Question q = new Question("Care este rezultatul parantezei "+expPar+" ?", tree.getExp(node));
                EandQ eq = new EandQ(equation,q);
                questions.add(eq);
                sameTerms = true;
            }
            String after = tree.getExp(node);
            if(!sameTerms && !expPar.equals(after)){
                Question q = new Question("Care este rezultatul parentezei "+expPar+" dupa reducerea termenilor?", tree.getExp(node));
                EandQ eq = new EandQ(equation,q);
                questions.add(eq);
            }
            if(node.parenthesis && !fromDivision){
                String beforeBalance = tree.getExp(node);
                exp = getEquation();
                tree.balanceTreeXSqr(tree.head, node);
                String expAfter = tree.getExpression();
                if(!exp.equals(expAfter)){
                    Question q = new Question("Care este rezultatul dupa desfacerea parantezei "+beforeBalance+" ?", tree.getExpression());
                    EandQ eq = new EandQ(exp,q);
                    questions.add(eq);
                }
            }
        }

    }

    void solveDivision(ArrayList<ExpressionTree> LandR) {
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);

        checkDivision(left, left.head, right);
        checkDivision(right, right.head, left);

    }

    void checkDivision(ExpressionTree tree, Node node, ExpressionTree oppositeTree){
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
                    if(node.l.x && node.r.x){
                        node.x = false;
                    }else if(node.l.x){
                        node.x = true;
                    }
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
        ExpressionTree right =LandR.get(1);

        checkMultiplication(left, left.head);
        checkMultiplication(right,right.head);
    }

    void checkMultiplication(ExpressionTree tree, Node parent){
        if(parent == null) return;
        checkMultiplication(tree, parent.l);
        checkMultiplication(tree, parent.r);

        if(parent.data.equals("*")){
            if(parent.l.isNumber()&& parent.r.isNumber()){
                double number = tree.Evaluate(parent);
                parent.data = Double.toString(number);
                if(parent.l.x && parent.r.x){
                    parent.xSqr = true;
                    parent.x = false;
                }else if(parent.l.x || parent.r.x){
                    parent.x = true;
                    parent.xSqr = false;
                }else{
                    parent.x = false;
                    parent.xSqr = false;
                }
                parent.l = null;
                parent.r = null;
            }
        }

    }
    ExpressionTree NecessaryTransformations(ExpressionTree tree){
        tree.ParenthesisCheck(tree.head);
        tree.ReplaceMinus(tree.head);
        tree.ReplaceMultiplication(tree.head);
        return tree;
    }
    boolean isOnlyOneNumber(String str) {
        return str.matches("-?[0-9]+x?");
    }
    ExpressionTree Convert(String exp){
        ConvertToPostFix post;
        post = new ConvertToPostFix(exp);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(post.Convert().split(",")));
        ExpressionTree t = new ExpressionTree(list);
        t.ParenthesisCheck(t.head);
        return t;
    }
}