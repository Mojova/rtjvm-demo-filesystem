package com.rtjvm.scala.oop.files

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry]) extends DirEntry(parentPath, name) {
  def replaceEntry(entryName: String, newEntry: Directory): Directory = ???

  def findEntry(entryName: String): DirEntry = ???

  def addEntry(newEntry: DirEntry): Directory = ???

  def findDescendant(allDirsInPath: List[String]): Directory = ???

  def getAllDirectoriesInPath(): List[String] = path.substring(1).split(Directory.SEPARATOR).toList

  def hasEntry(entry: String): Boolean = ???

  override def asDirectory: Directory = this
}

object Directory {
  val SEPARATOR = "/"
  val ROOTPATH = "/"

  def ROOT: Directory = Directory.empty("", "")
  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())
}
