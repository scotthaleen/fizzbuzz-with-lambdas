#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

def isaFizzBuzz(p, sz):
    return lambda i : sz if p(i) else None

isFizz = isaFizzBuzz(lambda x: x % 3 == 0, "Fizz")
isBuzz = isaFizzBuzz(lambda x: x % 5 == 0, "Buzz")

conditions = lambda x: [fn(x) for fn in 
                   [isFizz, isBuzz]]

def combine(a,b):
    return "%s%s" % (a or '', b or '')

if __name__ == "__main__":

    limit = int(sys.argv[1]) if len(sys.argv) > 1 else 30

    for s in map(lambda i: reduce(combine, conditions(i)) or str(i), range(1, limit+1)):
        print s
