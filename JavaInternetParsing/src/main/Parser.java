package main;

import listamparser.Finder;
import universals.Universals;

public class Parser {
	public static void main(String[] args) throws Exception {
		Finder finder = new Finder(Universals.getData("res/listamparserRes/cars.xls"));
		finder.search("Mercedes", 1).exportResTo();
	}
}
