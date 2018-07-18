package main;

import listamparser.Finder;
import universals.Universals;

public class Parser {
	public static void main(String[] args) throws Exception {
		Finder finder = new Finder(Universals.getData("res/listamparserRes/cars.xls"));
		finder.search("BMW", 1).exportResTo();
	}
}
