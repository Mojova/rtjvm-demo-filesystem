package com.rtjvm.scala.oop.files

import com.rtjvm.scala.oop.filesystem.FileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry]) extends DirEntry(parentPath, name) {
  def removeEntry(entryName: String): Directory = {
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(x => !x.name.equals(entryName)))
  }

  def isRoot: Boolean = parentPath.isEmpty

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory = new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

  def findEntry(entryName: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry = {
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)
    }

    findEntryHelper(entryName, contents)
  }

  def addEntry(newEntry: DirEntry): Directory = new Directory(parentPath, name, contents :+ newEntry)

  def findDescendant(path: List[String]): Directory = {
    if (path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)
  }

  def findDescendant(relativePath: String): Directory = {
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)
  }

  def getAllDirectoriesInPath: List[String] = path.substring(1).split(Directory.SEPARATOR).toList.filter(x => !x.isEmpty)

  def hasEntry(entry: String): Boolean = findEntry(entry) != null

  override def asDirectory: Directory = this

  def getType: String = "Directory"

  override def asFile: File = throw new FileSystemException("Directory can’t be converted to a file.")

  override def isDirectory: Boolean = true

  override def isFile: Boolean = false
}

object Directory {
  val SEPARATOR = "/"
  val ROOTPATH = "/"

  def ROOT: Directory = Directory.empty("", "")
  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())
}
