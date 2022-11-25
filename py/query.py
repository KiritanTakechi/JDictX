import stardict
import sys

book = stardict.StarDict(sys.argv[2])
print(book.query(sys.argv[1]))
