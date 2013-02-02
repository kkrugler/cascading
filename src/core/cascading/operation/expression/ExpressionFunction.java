/*
 * Copyright (c) 2007-2010 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Cascading is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cascading is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cascading.  If not, see <http://www.gnu.org/licenses/>.
 */

package cascading.operation.expression;

import java.beans.ConstructorProperties;

import cascading.flow.FlowProcess;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import org.codehaus.janino.ExpressionEvaluator;

/**
 * Class ExpressionFunction dynamically resolves a given expression using argument {@link Tuple} values. This {@link Function}
 * is based on the <a href="http://www.janino.net/">Janino</a> compiler.
 * <p/>
 * Specifically this function uses the Janino <a href="http://dist.codehaus.org/janino/javadoc/org/codehaus/janino/ExpressionEvaluator.html">ExpressionEvaluator</a>, thus the syntax from that class is inherited here.
 * <p/>
 * An expression may use field names directly as parameters in the expression, or field positions with the syntax
 * "$n", where n is an integer.
 * <p/>
 * Given an argument tuple with the fields "a" and "b", the following expression returns true: <br/>
 * <code>a + b == $0 + $1</code><br/>
 * <p/>
 * Further, the types of the tuple elements will be coerced into the given parameterTypes. Regardless of the actual
 * tuple element values, they will be converted to the types expected by the expression.
 * <p/>
 * Field names used in the expression should be valid Java variable names; for example, '+' or '-' are not allowed.
 * Also the use of a field name that begins with an upper-case character is likely to fail and should be avoided.
 */
public class ExpressionFunction extends ExpressionOperation implements Function<ExpressionOperation.Context>
  {
  /**
   * Constructor ExpressionFunction creates a new ExpressionFunction instance.
   * <p/>
   * This constructor assumes the given expression expects no input parameters. This is useful when
   * inserting random numbers for example, {@code (int) (Math.random() * Integer.MAX_VALUE) }.
   *
   * @param fieldDeclaration of type Fields
   * @param expression       of type String
   */
  @ConstructorProperties({"fieldDeclaration", "expression"})
  public ExpressionFunction( Fields fieldDeclaration, String expression )
    {
    super( fieldDeclaration, expression );

    if( !fieldDeclaration.isSubstitution() && fieldDeclaration.size() != 1 )
      throw new IllegalArgumentException( "fieldDeclaration may only declare one field, was " + fieldDeclaration.print() );
    }

  /**
   * Constructor ExpressionFunction creates a new ExpressionFunction instance.
   * <p/>
   * This constructor assumes all parameter are of the same type.
   *
   * @param fieldDeclaration of type Fields
   * @param expression       of type String
   * @param parameterType    of type Class
   */
  @ConstructorProperties({"fieldDeclaration", "expression", "parameterType"})
  public ExpressionFunction( Fields fieldDeclaration, String expression, Class parameterType )
    {
    super( fieldDeclaration, expression, parameterType );

    if( !fieldDeclaration.isSubstitution() && fieldDeclaration.size() != 1 )
      throw new IllegalArgumentException( "fieldDeclaration may only declare one field, was " + fieldDeclaration.print() );
    }

  /**
   * Constructor ExpressionFunction creates a new ExpressionFunction instance.
   * <p/>
   * This constructor expects all parameter type names to be declared with their types. Positional parameters must
   * be named the same as in the given expression with the "$" sign prepended.
   *
   * @param fieldDeclaration of type Fields
   * @param expression       of type String
   * @param parameterNames   of type String[]
   * @param parameterTypes   of type Class[]
   */
  @ConstructorProperties({"fieldDeclaration", "expression", "parameterNames", "parameterTypes"})
  public ExpressionFunction( Fields fieldDeclaration, String expression, String[] parameterNames, Class[] parameterTypes )
    {
    super( fieldDeclaration, expression, parameterNames, parameterTypes );

    if( !fieldDeclaration.isSubstitution() && fieldDeclaration.size() != 1 )
      throw new IllegalArgumentException( "fieldDeclaration may only declare one field, was " + fieldDeclaration.print() );
    }

  /** @see Function#operate(cascading.flow.FlowProcess,cascading.operation.FunctionCall) */
  public void operate( FlowProcess flowProcess, FunctionCall<Context> functionCall )
    {
    functionCall.getOutputCollector().add( new Tuple( evaluate( functionCall.getContext(), functionCall.getArguments() ) ) );
    }

  }
