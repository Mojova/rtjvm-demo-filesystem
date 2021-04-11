package com.rtjvm.scala.oop.files

abstract class DirEntry(val parentPath: String, val name: String) {
  def isDirectory: Boolean

  def isFile: Boolean

  def path: String = {
    val separatorIfNecessary = {
      if (Directory.ROOTPATH.equals(parentPath)) ""
      else Directory.SEPARATOR
    }
    parentPath + separatorIfNecessary + name
  }

  def asDirectory: Directory

  def getType: String

  def asFile: File
}
