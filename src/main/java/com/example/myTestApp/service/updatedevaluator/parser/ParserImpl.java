package com.example.myTestApp.service.updatedevaluator.parser;

import com.example.myTestApp.exception.BadOperandException;
import com.example.myTestApp.exception.MissedLeftBracketException;
import com.example.myTestApp.exception.MissedRightBracketException;
import com.example.myTestApp.service.updatedevaluator.token.*;

import java.util.LinkedList;
import java.util.List;

public class ParserImpl implements Parser {
    @Override
    public Double evaluate(List<Token> tokens) {
        return customEvaluateTokens(new LinkedList(tokens));
    }

    @Override
    public Integer getNumOfOperands(List<Token> lexemes) {
        return lexemes.stream().filter(token -> token instanceof NumberToken).toList().size();
    }


//    private Double evaluateTokens(LinkedList<Token> tokens) {
//        if (tokens.isEmpty()) throw new RuntimeException("empty!");
//        var nextToken = tokens.removeFirst();
//        if (nextToken instanceof NumberToken) {
//            var leftOperand = ((NumberToken) nextToken).getNumber();
//            if (tokens.isEmpty() || (tokens.size() == 1 && tokens.getFirst() instanceof BracketToken && !((BracketToken) tokens.getFirst()).isOpen())) return leftOperand;
//            var operation = tokens.removeFirst();
//            if (operation instanceof OperationToken) {
//                var rightOperand = evaluateTokens(tokens);
//                return ((OperationToken) operation).doOperation(leftOperand, rightOperand);
//            } else throw new RuntimeException("operation expected!");
//        } else if (nextToken instanceof BracketToken) {
//            if (!((BracketToken) nextToken).isOpen()) throw new MissedLeftBracketException();
//            var result = evaluateTokens(tokens);
//            if (tokens.isEmpty()) throw new MissedRightBracketException();
//            var lastToken = tokens.removeFirst();
//            if (!(lastToken instanceof BracketToken) || ((BracketToken) lastToken).isOpen())
//                throw new RuntimeException();
//            return result;
//        } else throw new RuntimeException("unexpected token!");
//
//    }
    private Double customEvaluateTokens(LinkedList<Token> tokens) {
        if (tokens.isEmpty()) throw new RuntimeException("empty!");
        var nextToken = tokens.removeFirst();
        if (nextToken instanceof NumberToken) {
            var leftOperand = ((NumberToken) nextToken).getNumber();
            if (tokens.isEmpty() || (tokens.size() == 1 && tokens.getFirst() instanceof BracketToken && !((BracketToken) tokens.getFirst()).isOpen())) return leftOperand;
            var operation = tokens.removeFirst();
            if(operation instanceof OperationToken) {
                if(((OperationToken) operation).getOperationType().equals(OperationType.MULTIPLICATION) || ((OperationToken) operation).getOperationType().equals(OperationType.DIVISION)){
                    var rightToken = tokens.getFirst();
                    var res = 0.0;
                    if(rightToken instanceof NumberToken) {
                        res = ((OperationToken) operation).doOperation(leftOperand, ((NumberToken) rightToken).getNumber());
                        tokens.removeFirst();

                    } else if (rightToken instanceof BracketToken && ((BracketToken) rightToken).isOpen()) {
                        res = leftOperand * customEvaluateTokens(tokens);
                    }
                    tokens.addFirst(new NumberToken(res));
                    return customEvaluateTokens(tokens);
                } else {
                    return ((OperationToken) operation).doOperation(leftOperand, customEvaluateTokens(tokens));
                }
            } else throw new RuntimeException("operation expected!");
        } else if (nextToken instanceof BracketToken) {
            if (!((BracketToken) nextToken).isOpen()) throw new MissedLeftBracketException();
            var result = customEvaluateTokens(tokens);
            if (tokens.isEmpty()) throw new MissedRightBracketException();
            var lastToken = tokens.removeFirst();
            if (!(lastToken instanceof BracketToken) || ((BracketToken) lastToken).isOpen())
                throw new RuntimeException();
            return result;
        } else throw new RuntimeException("unexpected token!");
    }

}
