const CONST_A = {
  a: 1
};

console.log(CONST_A.a);
CONST_A.a = 2;
CONST_A["a"] = 1;

/**
 *
 * @const
 */
var CONST_B = {
  a: 1
};
console.log(CONST_B.a);

CONST_B.a = 2;
CONST_B["a"] = 1;

/**
 *
 * @define {number}
 */
var DEFINE = 3;
console.log(DEFINE);

/**
 * @enum
 */
var Enum = {
  VAR1: 1,
  VAR2: 2,
  VAR3: 3
};

console.log(Enum.VAR1);
console.log(Enum.VAR2);
console.log(Enum.VAR3);

var My = {};
/**
 *
 * @const
 */
My.CONST = 1;
console.log(My.CONST);
console.log(My.CONST);
console.log(My.CONST);

/**
 * @enum
 */
My.Enum = {
  VAR1: 1,
  VAR2: 2,
  VAR3: 3
};

console.log(My.Enum.VAR1);
console.log(My.Enum.VAR2);
console.log(My.Enum.VAR3);