\ : test dup <0? if 2 / else 2 * then ;
\ -4 test .
\ 1 test .

: test [: ?for 2 * 3 + next ;] execute ;
\ : test ?for 2 * 3 + next ;
1 1 test .


