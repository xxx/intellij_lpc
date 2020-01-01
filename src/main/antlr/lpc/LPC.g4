grammar LPC;

/* based on https://github.com/antlr/grammars-v4/blob/master/lpc/LPC.g4 */

/* Lex */

TypeModifier
    :   'nomask'
    |   'private'
    |   'protected'
    |   'public'
    |   'static'
    |   'varargs'
    ;

PragmaType
    :   'strict_types'
    |   'no_clone'
    |   'no_inheritance'
    |   'no_shadow'
    |   'resident'
    |   'save_binary'
    ;

StrictAssign
    :   '='
    ;

Assign
    :   StrictAssign
    |   '+='
    |   '-='
    |   '&='
    |   '|='
    |   '^='
    |   '<<='
    |   '>>='
    |   '*='
    |   '%='
    |   '/='
    ;

PlusPlus    :   '++' ;

MinusMinus  :   '--' ;

And         :   '&' ;

AndAnd
    :   '&&'
    ;

Caret
    :   '^'
    ;

Or
    :   '|'
    ;

OrOr
    :   '||'
    ;

Equal
    :   '=='
    ;

LeftShift
    :   '<<'
    ;

RightShift
    :   '>>'
    ;

Not
    :   '!'
    ;

NotEqual
    :   '!='
    ;

Plus
    : '+'
    ;

Compare
    :   Less
    |   LessEqual
    |   Great
    |   GreatEqual
    ;

/*
    [1..<1] did not allow Less
*/
Less
    :   '<'
    ;

LessEqual
    :   '<='
    ;

Great
    :   '>'
    ;

GreatEqual
    :   '>='
    ;

/* Reserved */
Arrow
    :   '->'
    ;

Compose
    :   '@'
    ;

BasicType
    :   'float'
    |   'function'
    |   'int'
    |   'mapping'
    |   'mixed'
    |   'object'
    |   'string'
    |   'void'
    ;

Break
    :   'break'
    ;

Catch
    :   'catch'
    ;

Colon
    :   ':'
    ;

ColonColon
    :   '::'
    ;

Continue
    :   'continue'
    ;

Efun
    :   'efun'
    ;

Ellipsis
    :   '...'
    ;

Else
    :   'else'
    ;

If
    :   'if'
    ;

Inherit
    :   'inherit'
    ;

Return
    :   'return'
    ;

For
    :   'for'
    ;

Foreach
    :   'foreach'
    ;

In
    :   'in'
    ;

Switch
    :   'switch'
    ;

Case
    :   'case'
    ;

While
    :   'while'
    ;

Do
    :   'do'
    ;

Default
    :   'default'
    ;

Operator
    :   'operator'
    ;

Question
    :   '?'
    ;

Range
    :   '..'
    ;

// These make brace matching more difficult, as intellij doesn't know to
// skip over the first char of a closing multi-char match when typing it
mapping_open
    :   LeftParen (Whitespace|Newline)* LeftBracket
    ;

mapping_close
    :   RightParen (Whitespace|Newline)* RightBracket
    ;

array_open
    :   LeftParen (Whitespace|Newline)* LeftBrace
    ;

array_close
    :   RightParen (Whitespace|Newline)* RightBrace
    ;

mapping_empty
    : mapping_open (Whitespace|Newline)* mapping_close
    | LeftParen OperatorIndex RightParen
    ;

array_empty
    : array_open (Whitespace|Newline)* array_close
    ;

LeftParen
    :   '('
    ;

RightParen
    :   ')'
    ;

LeftBrace
    :   '{'
    ;

RightBrace
    :   '}'
    ;

LeftBracket
    :   '['
    ;

RightBracket
    :   ']'
    ;

SemiColon
    :   ';'
    ;

Comma
    :   ','
    ;

Number
    :   IntegerConstant
    ;

/* Pre processing */
ComplexDefine
    :   '#' Whitespace* 'define' (~[\\\r\n] | '\\\\' '\r'? '\n' | '\\'. )*
//        -> channel(HIDDEN)
    ;

ComplexInclude
    :   '#' Whitespace* 'include'  ~[\r\n]*
//        -> channel(HIDDEN)
    ;

ComplexPragma
    :   '#' Whitespace* 'pragma' Whitespace+ PragmaType Whitespace* [\r\n]+
//        -> channel(HIDDEN)
    ;

ComplexPreprocessor
    :   '#' ~[\r\n]*
//        -> channel(HIDDEN)
    ;

Real
    :   FractionalConstant
    ;

fragment
FractionalConstant
    :   DigitSequence? '.' DigitSequence
    |   DigitSequence '.'   {_input.LA(1) != '.'}?    // java
    ;

DigitSequence
    :   Digit+
    ;


Identifier
    :   IdentifierNondigit
        (   IdentifierNondigit
        |   Digit
        )*
    ;

fragment
IdentifierNondigit
    :   Nondigit
    //|   UniversalCharacterName
    //|   // other implementation-defined characters...
    ;

fragment
Nondigit
    :   [a-zA-Z_]
    ;

fragment
Digit
    :   [0-9]
    ;

fragment
IntegerConstant
    :   DecimalConstant
    |   OctalConstant
    |   HexadecimalConstant
    ;

fragment
DecimalConstant
    :   NonzeroDigit Digit*
    ;

fragment
OctalConstant
    :   '0' OctalDigit*
    ;

fragment
HexadecimalConstant
    :   HexadecimalPrefix HexadecimalDigit+
    ;

fragment
HexadecimalPrefix
    :   '0' [xX]
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
HexadecimalDigit
    :   [0-9a-fA-F]
    ;

String
    :   '"' SCharSequence? '"'
    ;

CharacterConstant
    :   '\'' SingleChar? '\''
    ;

fragment
SCharSequence
    :   SChar+
    ;

fragment
SChar
    :   ~["\\\r\n]                       // BUG, removed \" from here
    |   EscapeSequence
    |   '\\\n'   // Added line
    |   '\\\r\n' // Added line
    |   '\n'     // lpc want this
    |   '\r\n'   // lpc want this, too
    ;

fragment
SingleChar
    :   '"'
    |   SChar
    ;

fragment
EscapeSequence
    :   SimpleEscapeSequence
    |   OctalEscapeSequence
    |   HexadecimalEscapeSequence
    |   UniversalCharacterName
    ;

fragment
UniversalCharacterName
    :   '\\u' HexQuad
    |   '\\U' HexQuad HexQuad
    ;

fragment
HexQuad
    :   HexadecimalDigit HexadecimalDigit HexadecimalDigit HexadecimalDigit
    ;

fragment
HexadecimalEscapeSequence
    :   '\\x' HexadecimalDigit+
    ;

fragment
OctalEscapeSequence
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' OctalDigit OctalDigit OctalDigit
    ;

fragment
SimpleEscapeSequence
    :   '\\' ['"?abfnrtv\\]
    |   '\\' [^+.[{}\]!@#$%&*()_=\-|/<>]    // WTF: LPC escapes these characters (inface, only warn in lpc)
    ;

BlockComment
    :   '/*' .*? '*/'
        -> channel(HIDDEN)
    ;

LineComment
    :   '//' ~[\r\n]*
        -> channel(HIDDEN)
    ;

Whitespace
    :   [ \t]+
        -> channel(HIDDEN)
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> channel(HIDDEN)
    ;

BadChar
    : .
      -> channel(HIDDEN)
    ;

lpc_program
    :   program EOF
    ;

program
    :   program definition possible_semi_colon
    |   /* empty */
    ;

possible_semi_colon
    :   /* empty */
    |   SemiColon
    ;

definition
    :   global_variable_definition
    |   function_definition
    |   inheritance
    |   preprocessor_directive
    ;

function_definition
    :   function_implementation
    |   function_prototype
    ;

function_implementation
    :   data_type optional_star Identifier LeftParen argument RightParen block
    ;

function_prototype
    :   data_type optional_star Identifier LeftParen argument RightParen SemiColon
    ;

type_modifier_list
    :   /* empty */
    |   TypeModifier type_modifier_list
    ;

global_variable_definition
    :   data_type name_list SemiColon
    ;

name_list
    :   new_name
    |   new_name Comma name_list
    ;

new_name
    :   optional_star Identifier
    |   optional_star Identifier StrictAssign expr0
    ;

expr0
    :   expr4 assign expr0
    |<assoc=right>   expr0 Compose expr0
    |   expr0 Question expr0 Colon expr0
    |   expr0 OrOr expr0
    |   expr0 AndAnd expr0
    |   expr0 Or expr0
    |   expr0 Caret expr0
    |   expr0 And expr0
    |   expr0 Equal expr0
    |   expr0 NotEqual expr0
    |   expr0 Compare expr0
    |   expr0 LeftShift expr0
    |   expr0 RightShift expr0

    |   expr0 ('*'|'%'|'/') expr0
    |   expr0 ('+'|'-') expr0

    |   cast expr0
    |   PlusPlus expr4
    |   MinusMinus expr4
    |   Not expr0
    |   '~' expr0
    |   '-' expr0
    |   expr4 PlusPlus   /* normal precedence here */
    |   expr4 MinusMinus
    |   expr4

    |   Number
    |   Real
    ;

assign
    : Assign
    | StrictAssign
    ;

pointer_operator
    :   Equal
    |   NotEqual
    |   OperatorIndex
    |   Plus
    |   Plus
    |   '-'
    |   '*'
    |   '/'
    |   '%'
    |   Caret
    |   Or
    |   And
    |   OrOr
    |   AndAnd
    |   Compare
    |   RightShift
    |   LeftShift
    ;

OperatorIndex
    : '[]'
    ;

comma_expr
    :   expr0
    |   comma_expr Comma expr0
    ;

lvalue_list
    :   /* empty */
    |   Comma expr4 lvalue_list
    ;

cast
    :   LeftParen BasicType optional_star RightParen
    ;

expr4
    :   function_call
    |   expr4 function_arrow_call
    |   Identifier

    |   expr4 LeftBracket comma_expr Range comma_expr RightBracket
    |   expr4 LeftBracket comma_expr Range RightBracket
    |   expr4 LeftBracket Range comma_expr RightBracket
    |   expr4 LeftBracket comma_expr RightBracket
    |   string
    |   CharacterConstant
    |   LeftParen comma_expr RightParen
    |   catch_statement

    |   mapping_empty
    |   array_empty
    |   mapping_open expr_list3 RightBracket RightParen
    |   array_open expr_list RightBrace RightParen
    |   function_pointer
    |   expr4 function_arrow_pointer
    ;

function_pointer
    :   Identifier
    |   And Operator LeftParen pointer_operator RightParen LeftParen partial_expr_list RightParen
    |   And Identifier LeftParen partial_expr_list RightParen
    |   And expr4 Arrow Identifier LeftParen partial_expr_list RightParen
    |   And Arrow Identifier LeftParen partial_expr_list RightParen
    ;

function_arrow_pointer
    : Arrow Identifier
    ;

partial_expr_list
    : /* empty */
    | partial_expr_list2
    | partial_expr_list2 Comma
    ;

partial_expr_list2
    :   partial_expr_list_node
    |   partial_expr_list2 Comma partial_expr_list_node
    ;

partial_expr_list_node
    : /* empty */
    |   expr_list_node
    ;

catch_statement
    :   Catch LeftParen comma_expr RightParen
    ;

expr_list
    :   /* empty */
    |   expr_list2
    |   expr_list2 Comma
    ;

expr_list3
    :   /* empty */
    |   expr_list4
    |   expr_list4 Comma
    ;

expr_list4
    :   assoc_pair
    |   expr_list4 Comma assoc_pair
    ;

assoc_pair
    :   expr0 ':' expr0
    ;

expr_list2
    :   expr_list_node
    |   expr_list2 Comma expr_list_node
    ;

expr_list_node
    :   expr0
    |   expr0 Ellipsis
    ;

string
    :   string_con2
    ;

string_con2
    :   String
    |   string_con2 String
    ;

string_con1
    :   string_con2
    |   LeftParen string_con1 RightParen
    |   string_con1 '+' string_con1
    ;

// combine into expr4
function_call
    :   efun_override LeftParen expr_list RightParen
    |   function_name_call  //function_name LeftParen expr_list RightParen
    |   function_arrow_call //expr4 Arrow identifier LeftParen expr_list RightParen
    ;

function_name_call
    :   function_name LeftParen expr_list RightParen
    ;

function_arrow_call
    :   Arrow Identifier LeftParen expr_list RightParen
    ;

function_name
    :   Identifier
    |   ColonColon Identifier
    |   function_call_namespace ColonColon Identifier
    ;

function_call_namespace
    : Identifier
    ;

efun_override
    :   Efun ColonColon Identifier
    ;

block
    :   LeftBrace statements RightBrace
    ;

statements
    :   /* empty */
    |   statement statements
    |   local_variable_definition statements
    ;

preprocessor_directive
    : ComplexPragma
    | ComplexInclude
    | ComplexDefine
    | ComplexPreprocessor
    ;

local_variable_definition
    :   BasicType local_name_list SemiColon
    ;

local_name_list
    :   new_local_def
    |   new_local_def Comma local_name_list
    ;

new_local_def
    :   optional_star Identifier
    |   optional_star Identifier StrictAssign expr0
    ;

statement
    :   comma_expr SemiColon
    |   cond
    |   while_statement
    |   do_statement
    |   switch_statement
    |   return_statement

    |   block
    |   for_loop
    |   foreach_loop

    |   /* empty */ SemiColon
    |   Break SemiColon
    |   Continue SemiColon
    |   preprocessor_directive
    ;

while_statement
    :   While LeftParen comma_expr RightParen statement
    ;

do_statement
    :   Do statement While LeftParen comma_expr RightParen SemiColon
    ;

switch_statement
    :   Switch LeftParen comma_expr RightParen LeftBrace local_declarations case_statement switch_block RightBrace
    ;

local_declarations
    :   /* empty */
    |   local_declarations local_variable_definition
    ;

case_statement
    :   Case case_label ':'
    |   Case case_label Range case_label ':'
    |   Default ':'
    ;

switch_block
    :   case_statement switch_block
    |   statement switch_block
    |   /* empty */
    ;

case_label
    :   constant
    |   CharacterConstant
    |   string_con1
    ;

constant
    :   constant '|' constant
    |   constant '^' constant
    |   constant '&' constant
    |   constant Equal constant
    |   constant NotEqual constant
    |   constant Compare constant
    |   constant '<' constant
    |   constant LeftShift constant
    |   constant RightShift constant
    |   LeftParen constant RightParen

    |   constant '*' constant
    |   constant '%' constant
    |   constant '/' constant

    |   constant '-' constant
    |   constant '+' constant
    |   Number
    |   '-' Number
    |   Not Number
    |   '~' Number
    ;

foreach_loop
    :   Foreach LeftParen single_new_local_def ':' expr0 RightParen statement
    |   Foreach LeftParen single_new_local_def Comma single_new_local_def ':' expr0 RightParen statement
    ;

for_loop
    :   For LeftParen first_for_expr SemiColon for_expr SemiColon for_expr RightParen statement
    ;

first_for_expr
    :   for_expr
    |   single_new_local_def_with_init
    ;

single_new_local_def_with_init
    :   single_new_local_def StrictAssign expr0
    ;

single_new_local_def
    :   BasicType optional_star Identifier
    ;

for_expr
    :   /* EMPTY */
    |   comma_expr
    ;

return_statement
    :   Return SemiColon
    |   Return comma_expr SemiColon
    ;

cond
    :   If LeftParen comma_expr RightParen statement optional_else_part
    ;

optional_else_part
    :   /* empty */
    |   Else statement
    ;

argument
    :   /* empty */
    |   argument_list
    |   argument_list Ellipsis
    ;

argument_list
    :   argument_definition
    |   argument_list Comma argument_definition
    ;

argument_definition
    :   BasicType optional_star
    |   single_new_local_def optional_arg_assignment
    ;

optional_arg_assignment
    : /* empty */
    |   StrictAssign expr0
    ;

inheritance
    :   Inherit string_con1 SemiColon
    |   Inherit string_con1 Identifier SemiColon
    ;

data_type
    :   type_modifier_list opt_basic_type
    |   opt_basic_type
    ;

opt_basic_type
    :   BasicType
    |   /* empty */
    ;

optional_star
    :   /* empty */
    |   '*'
    ;
