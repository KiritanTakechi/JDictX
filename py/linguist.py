#! /usr/bin/env python
# -*- coding: utf-8 -*-
#======================================================================
#
# linguist.py - 
#
# Created by skywind on 2017/03/22
# Last change: 2017/03/22 13:44:42
#
#======================================================================
import sys, os, time

# https://www.nodebox.net/code/index.php/Linguistics

#----------------------------------------------------------------------
# python 2/3 compatible
#----------------------------------------------------------------------
if sys.version_info[0] >= 3:
	long = int
	xrange = range
	unicode = str


#----------------------------------------------------------------------
# 词形变换
#----------------------------------------------------------------------
class WordHelper (object):

	# 初始化
	def __init__ (self):
		self.__lemmatizer = None

	# 取得 WordNet 的词定义
	def definition (self, word, txt = False):
		from nltk.corpus import wordnet as wn
		syns = wn.synsets(word)
		output = []
		for syn in syns:
			name = syn.name()
			part = name.split('.')
			mode = part[1]
			output.append((mode, syn.definition()))
		if txt:
			output = '\n'.join([ (m + ' ' + n) for m, n in output ])
		return output

	# 取得动词的：-ing, -ed, -en, -s
	# NodeBox 的 Linguistics 软件包 11487 个动词只能处理 6722 个 
	def verb_tenses (self, word):
		word = word.lower()
		if ' ' in word:
			return None
		import en
		if not en.is_verb(word):
			return None
		tenses = {}
		try:
			tenses['i'] = en.verb.present_participle(word)
			tenses['p'] = en.verb.past(word)
			tenses['d'] = en.verb.past_participle(word)
			tenses['3'] = en.verb.present(word, person = 3, negate = False)
		except:
			return None
		valid = True
		for k in tenses:
			v = tenses[k]
			if not v:
				valid = False
				break
			elif "'" in v:
				valid = False
				break
		if not valid:
			return None
		return tenses

	# 名词的复数：有时候不可数名词也会被加上 -s，需要先判断是否可数（语料库）
	def noun_plural (self, word, method = 0):
		plural = None
		if method == 0:
			import en
			plural = en.noun.plural(word)
		elif method == 1:
			import pattern.en
			plural = pattern.en.pluralize(word)
		elif method == 2:
			import inflect
			plural = inflect.pluralize(word)
		elif method < 0:
			plural = self.noun_plural(word, 0)
			if not plural:
				plural = self.noun_plural(word, 1)
				if not plural:
					try:
						import inflect
						plural = inflect.pluralize(word)
					except:
						pass
		if not plural:
			return None
		return plural

	# 求解比较级
	def adjective_comparative (self, word):
		import pattern.en
		return pattern.en.comparative(word)

	# 求解最高级
	def adjective_superlative (self, word):
		import pattern.en
		return pattern.en.superlative(word)

	# 求解复数，使用 pattern.en 软件包
	def pluralize (self, word):
		import pattern.en
		return pattern.en.pluralize(word)

	# 取得所有动词
	def all_verbs (self):
		import en
		words = []
		for n in en.wordnet.all_verbs():
			words.append(n.form)
		return words

	# 取得所有副词
	def all_adverbs (self):
		import en
		words = []
		for n in en.wordnet.all_adverbs():
			words.append(n.form)
		return words

	# 取得所有形容词
	def all_adjectives (self):
		import en
		words = []
		for n in en.wordnet.all_adjectives():
			words.append(n.form)
		return words

	# 取得所有名词
	def all_nouns (self):
		import en
		words = []
		for n in en.wordnet.all_nouns():
			words.append(n.form)
		return words

	# 返回原始单词
	def lemmatize (self, word, pos = 'n'):
		word = word.lower()
		if self.__lemmatizer is None:
			from nltk.stem.wordnet import WordNetLemmatizer
			self.__lemmatizer = WordNetLemmatizer()
		return self.__lemmatizer.lemmatize(word, pos)


#----------------------------------------------------------------------
# global
#----------------------------------------------------------------------
tools = WordHelper()

#----------------------------------------------------------------------
# WordRoot
#----------------------------------------------------------------------
class WordRoot (object):

	def __init__ (self, root):
		self.root = root
		self.count = 0
		self.words = {}

	def add (self, c5, word, n = 1):
		if c5 and word:
			term = (c5, word)
			if not term in self.words:
				self.words[term] = n
			else:
				self.words[term] += n
			self.count += n
		return True

	def dump (self):
		output = []
		for term in self.words:
			c5, word = term
			output.append((c5, word, self.words[term]))
		output.sort(key = lambda x: (x[2], x[0]), reverse = True)
		return output

	def __len__ (self):
		return len(self.words)
	
	def __getitem__ (self, key):
		return self.words[key]
