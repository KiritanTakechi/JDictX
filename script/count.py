import sys

from ECDICT import stardict

book = stardict.StarDict(sys.argv[1])
print(book.count())
