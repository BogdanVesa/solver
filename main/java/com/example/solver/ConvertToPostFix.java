package com.example.solver;

import java.util.ArrayDeque;
import java.util.Deque;

public class ConvertToPostFix {
    String exp;

    public ConvertToPostFix(String exp){
        this.exp = exp;
    }

    public String Convert(){
        String result = new String("");

        Deque<Character> stack = new ArrayDeque<Character>();

        for (int i = 0; i < this.exp.length(); ++i) {
            char c = this.exp.charAt(i);

            if (Character.isLetterOrDigit(c))
                result += c;

            else if (c == '(')
                stack.push(c);


            else if (c == ')') {
                while (!stack.isEmpty()
                        && stack.peek() != '(') {
                    result +=',';
                    result += stack.peek();
                    stack.pop();
                }

                stack.pop();
            }
            else
            {
                result +=',';
                while (!stack.isEmpty()
                        && pre(c) <= pre(stack.peek())) {

                    result += stack.peek();
                    result +=',';
                    stack.pop();
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                return "Invalid Expression";
            result +=',';
            result += stack.peek();
            stack.pop();
        }

        return result;
    }

    int pre(char c){
        if(c == '+' || c=='-'){
            return 1;
        }
        if(c == '*' || c=='/'){
            return 2;
        }
        if(c == '^'){
            return 3;
        }
        return -1;
    }

}