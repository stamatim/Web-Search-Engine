# Web Search Engine

COM S 311 Programming Assignment 1 - Fall 2019
Authors: Stamatios Morellas & Jack Creighton


## Assignment Description
We are assigned with the task of implementing a rudimentary web search engine. Very
broadly, *a web search engine has two components*:
1. offline computation
2. online computation
where the search engine receives a search query from the user and outputs ranked list of web
pages that are relevant to the search query.

**The offline computation has two parts**:
1. Crawl the web and download contents of web pages and construct the web graph. Using the
web graph structure, determine the importance for each web page.
2. Based on the contents of the web-pages, build a data structure known as inverted index. The
inverted index is designed so that the online computation—retrieving a ranked list of pages
relevant to a search query—is done very efficiently.

In online phase, the user inputs a query and the search engine, upon receiving a search query
from the user, outputs a ranked list of web pages that are relevant to the search query. This
phase uses the importance (computed in part 1 of the offline computation) and the inverted index
(constructed in the second part of the offline computation) to determine and output a ranked list
of web-pages.

We are to implement the classes `Crawler`and `Index` in addition to anything else we need for our implementation.
