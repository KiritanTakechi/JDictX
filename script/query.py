import sys
import json

from ECDICT import stardict

try:
    book = stardict.StarDict(sys.argv[2])
    print(json.dumps(book.query(sys.argv[1])))
except IndexError:
    print("请输入单词！")