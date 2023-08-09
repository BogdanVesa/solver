package com.example.solver;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Arrays;

public class SecondDegree implements Solve{
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

        if(isADDegreeSmaller()){
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
        OpenParentheses(LandR);

        before = getEquation();
        solveMultiplication(LandR);
        after = getEquation();
        if(!after.equals(before)){
            EandQ eq10 = new EandQ(getEquation(),new Question());
            questions.add(eq10);
        }

        MoveNumber(LandR);

        String equation = getEquation();

        ArrayList<Node> numbersXSqr = numbersXSqr(LandR.get(0));
        ArrayList<Node> numbersX = numbersX(LandR.get(0));
        ArrayList<Node> numbers = numbers(LandR.get(0));
        int a = (int) sumNodes(numbersXSqr);
        int b = (int) sumNodes(numbersX);
        int c = (int) sumNodes(numbers);
        Question q1 = new Question("Care este valoare lui a?",Integer.toString(a));
        Question q2 = new Question("Care este valoare lui b?",Integer.toString(b));
        Question q3 = new Question("Care este valoare lui c?",Integer.toString(c));
        EandQ eq1 = new EandQ(equation,q1);
        questions.add(eq1);
        EandQ eq2 = new EandQ(equation,q2);
        questions.add(eq2);
        EandQ eq3 = new EandQ(equation,q3);
        questions.add(eq3);
        int delta = b*b-4*a*c;
        String afterMove = getGeneralForm(a,b,c);

        Question q5 = new Question("Care este valoare lui delta?",Integer.toString(delta));
        EandQ eq5 = new EandQ(afterMove,q5);
        questions.add(eq5);



        if(delta>0){
            delta = (int) Math.sqrt(delta);
            a = 2*a;
            double x1 = (-b-delta);
            double x2 = (-b+delta);

            String x= getXValue(x1, a);

            Question q6 = new Question("Care este valoare lui x1?",x);
            EandQ eq6 = new EandQ(afterMove,q6);
            questions.add(eq6);

            x= getXValue(x2, a);

            Question q7 = new Question("Care este valoare lui x2?",x);
            EandQ eq7 = new EandQ(afterMove,q7);
            questions.add(eq7);
        }else if (delta ==0){
            double x1 = (-b-delta);
            String x = getXValue(x1, 2*a);
            Question q8 = new Question("Care este valoare lui x?",x);
            EandQ eq8 = new EandQ(afterMove,q8);
            questions.add(eq8);
        }
        return questions;
    }

    String getXValue(double x, int a){
        String result="";
        if((x/a)%1==0){
            int x1= (int) x/a;
            result = Integer.toString(x1);
        }else{
            String sign="";
            if(x<0){
                sign = "-";
                x = -1*x;
            }
            if(a<0){
                sign = "-";
                a = -1*a;
            }
            if(x%1==0){
                int x2 = (int)x;
                result =sign+ x2+"/"+a;
            }else {
                result =sign + x + "/" + a;
            }
        }
        return result;
    }

    boolean isADDegreeSmaller(){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);
        if(left.isContainXSqr(left.head)){
            return false;
        }
        if(right.isContainXSqr(right.head)){
            return false;
        }
        return true;
    }

    void callRightDegree(){
        Equation eq = new Equation(getEquation());
        eq.setType();
        ArrayList<EandQ> q = eq.solve();

        questions.addAll(q);
    }

    String getGeneralForm(int a, int b, int c){
        String result="";
        result +=a+"x^2";
        if(b>0){
            result+="+"+b+"x";
        }else if(b<0){
            result+=b+"x";
        }
        if(c>0){
            result+="+"+c;
        }else if(c<0){
            result+=c;
        }
        result+="=0";
        return result;
    }
    double sumNodes(ArrayList<Node> nodes){
        double result = 0;
        if(nodes.size()==0){
            return result;
        }
        String exp =nodes.get(0).data;
        for(int i =1;i<nodes.size();i++){

            exp+="+"+nodes.get(i).data;
        }
        Expression e = new Expression(exp);
        result = e.calculate();
        return result;
    }
    ArrayList<Node> numbersXSqr(ExpressionTree tree){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = tree.findNumberXSqr(tree.head);
        while(number != null){
            result.add(number);
            number = tree.findNumberXSqr(tree.head);
        }
        return result;
    }
    ArrayList<Node> numbersX(ExpressionTree tree){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = tree.findNumber(tree.head, true);
        while (number !=null){
            result.add(number);
            number = tree.findNumber(tree.head,true);
        }
        return result;
    }
    ArrayList<Node> numbers(ExpressionTree tree){
        ArrayList<Node> result = new ArrayList<Node>();
        Node number = tree.findNumber(tree.head, false);
        while(number !=null){
            result.add(number);
            number = tree.findNumber(tree.head, false);
        }
        return result;
    }

    ExpressionTree NecessaryTransformations(ExpressionTree tree){
        tree.ParenthesisCheck(tree.head);
        tree.ReplaceMinus(tree.head);
        tree.ReplaceMultiplication(tree.head);
        tree.ReplacePower(tree.head);
        return tree;
    }
    ExpressionTree Convert(String exp){
        ConvertToPostFix post;
        post = new ConvertToPostFix(exp);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(post.Convert().split(",")));
        ExpressionTree t = new ExpressionTree(list);
        t.ParenthesisCheck(t.head);
        return t;
    }
    void MoveNumber(ArrayList<ExpressionTree> LandR){
        ExpressionTree left = LandR.get(0);
        ExpressionTree right = LandR.get(1);
        left.Inorder(left.head);
        System.out.println();
        right.Inorder(right.head);
        System.out.println();
        String equation = getEquation();

        ArrayList<Node> numbersXSqr = numbersXSqr(right);
        ArrayList<Node> numbersX = numbersX(right);
        ArrayList<Node> numbers = numbers(right);
        right.CleanTree(right.head);


        AddNumbers(left, numbersXSqr);
        AddNumbers(left, numbersX);
        AddNumbers(left, numbers);
        Question q1 = new Question("Cum arata ecuatia dupa mutarea numerelor", getEquation());
        EandQ eq1 = new EandQ(equation,q1);
        questions.add(eq1);
    }
    void AddNumbers(ExpressionTree tree, ArrayList<Node> nodes){
        Node plus;
        if(nodes.size()==0){
            return;
        }
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
    }
    void OpenParentheses(ArrayList<ExpressionTree> trees){
        ExpressionTree left = trees.get(0);
        ExpressionTree right = trees.get(1);

        solveParentheses(left.head, left);
        solveParentheses(right.head, right);
    }
    void solveParentheses(Node node,ExpressionTree tree ){
        if(node == null) return;
        solveParentheses(node.l, tree);
        solveParentheses(node.r, tree);

        if(node.data.equals("*")&& tree.subParenthesis(node)){
            solveParentheses(node, tree);
        }

        if(node.parenthesis){
            checkMultiplication(tree, node);
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
            tree.solveParenthesis(node);
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
        left.getExpression();
        checkDivision(right, right.head, left);
        right.getExpression();
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
                fromDivision = true;
                solveParentheses(node, tree);
            }
            if((node.l != null && node.r !=null)&&(node.l.isNumber() && node.r.isNumber())){
                double number = tree.Evaluate(node);
                if(number %1 == 0){
                    String equation = getEquation();
                    String exp = tree.getExp(node);
                    int n = (int) number;
                    node.data = Integer.toString(n);
                    if(node.l.xSqr && node.r.xSqr){
                        node.x = false;
                        node.xSqr = false;
                    }else if(node.l.xSqr && node.r.x){
                        node.x = true;
                        node.xSqr = false;
                    } else if(node.l.xSqr){
                        node.xSqr = true;
                    } else if(node.l.x && node.r.x){
                        node.x = false;
                        node.xSqr = false;
                    } else if(node.l.x){
                        node.x = true;
                        node.xSqr = false;
                    } else{
                        node.x = false;
                        node.xSqr = false;
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
                return;
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

        if(parent.parenthesis || parent.isNumber() || tree.op(parent.data) == 2){
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
            if(parent.l.isNumber() && parent.r.isNumber()){
                double number = tree.Evaluate(parent);
                parent.data = Double.toString(number);
                if(parent.l.xSqr && !parent.r.xSqr){
                    parent.xSqr = true;
                    parent.x = false;
                }else if(parent.r.xSqr && !parent.l.xSqr) {
                    parent.xSqr = true;
                    parent.x = false;
                }else if(parent.l.x && parent.r.x){
                    parent.xSqr = true;
                    parent.x = false;
                }else if(parent.l.x || parent.r.x){
                    parent.x = true;
                }
                parent.l = null;
                parent.r = null;
            }
        }

    }
    boolean isOnlyOneNumber(String str) {
        return str.matches("-?[0-9]+x\\.[0-9]*?^?2?");
    }
}
