package com.example.solver;

import java.util.Objects;

public class Node
{
    String data ;
    Node l , r ;
    boolean parenthesis;
    boolean x;
    boolean xSqr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return parenthesis == node.parenthesis && x == node.x && xSqr == node.xSqr && data.equals(node.data) && Objects.equals(l, node.l) && Objects.equals(r, node.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, l, r, parenthesis, x, xSqr);
    }
    public Node(){

    }

    public Node(String data)
    {
        this.data = data ;
        this.l = null ;
        this.r = null ;
        this.parenthesis = false;
        this.x = false;
        this.xSqr =false;
    }

    public void copyNode(Node node){
        if(node == null) return;

        this.data = node.data;
        this.x = node.x;
        this.parenthesis = node.parenthesis;
        this.xSqr = node.xSqr;
        this.l = new Node();
        if(node.l == null){
            this.l = null;
        }else {
            this.l.copyNode(node.l);
        }
        this.r = new Node();
        if(node.r == null){
            this.r= null;
        }else{
            this.r.copyNode(node.r);
        }
    }

    public String getData(){
        String result = data;
        int n=0;
        if(this.isNumber()){
            double number = Double.parseDouble(data);

            if(number %1 == 0){
                n= (int) number;
                result = Integer.toString(n);

            }
        }
        if(x){
            if(n==1){
                result = "";
            }
            if(n==-1){
                result="-";
            }
            return result+"x";
        }
        if(xSqr){
            if(n==1){
                result = "";
            }
            if(n==-1){
                result="-";
            }
            return result + "x^2";
        }
        return result;
    }

    public void ContainsX(){
        if(data.equals("x")){
            data = "1";
            this.x = true;
        }
        if(data.contains("x")){
            data = data.replace("x","");
            this.x = true;
        }
    }

    public boolean isNumber(){
        return this.data.matches("-?[0-9]+\\.?[0-9]*");
    }
}
