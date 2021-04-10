package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{DirEntry, Directory}
import com.rtjvm.scala.oop.filesystem.State

abstract class CreateEntry(name: String) extends Command {
  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
    if (path.isEmpty) currentDirectory.addEntry(newEntry)
    else {
      val oldEntry = currentDirectory.findEntry(path.head).asDirectory
      currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
    }
  }

  def doCreateEntry(state: State, name: String): State = {
    val wd = state.wd
    // 1. all the directories in the full path
    val allDirsInPath = wd.getAllDirectoriesInPath

    // 2. create new directory entry in the working directory
    val newEntry: DirEntry = createSpecificEntry(state)

    // 3. update the whole directory struture starting from the root
    val newRoot: Directory = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. find new working directory instance given wd’s full path in the new directory structure
    val newWd: Directory = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage(s"Entry ${name} already exists")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(s"“${name}” must not contain separators")
    } else if (checkIllegal(name)) {
      state.setMessage(s"“${name}”: illegal entry name")
    } else {
      doCreateEntry(state, name)
    }
  }

  def createSpecificEntry(state: State): DirEntry
}
