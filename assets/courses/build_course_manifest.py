import os, fnmatch, json

def recursive_glob(treeroot, pattern):
  results = []
  for base, dirs, files in os.walk(treeroot):
    goodfiles = fnmatch.filter(files, pattern)
    results.extend(os.path.join(base, f) for f in goodfiles)
  return results

if __name__ == "__main__":
	allfiles = recursive_glob(".","*.*")
	manifest = {"classes": []}

	# TODO: Make this elegant and recursive.
	for filename in allfiles:
		parts = filename.split("/")[1:]
		if not (".DS_Store" in parts):
			if len(parts) >= 4:

				'''
				# Chapters and doclists are kept in the right order by numbers in the filename. Here we split those out.
				if "." in parts[1]:
					chapter_id = ".".join(parts[1].split(".")[1:])
				else:
					chapter_id = parts[1]
				if "." in parts[2]:
					doclist_id = ".".join(parts[2].split(".")[1:])
				else:
					doclist_id = parts[2]
				'''
				chapter_id = parts[1]
				doclist_id = parts[2]
				if "." in chapter_id and "." in doclist_id: # Make sure the chapter and doclist IDs won't break the app's class tree builder.
					class_already_entered = [ (eachclass["id"]==parts[0]) for eachclass in manifest["classes"] ]
					if not (True in class_already_entered):
						manifest["classes"].append({ "id": parts[0], "chapters": [] })
						class_already_entered.append(True)
					class_index = class_already_entered.index(True)

					chapter_already_entered = [ (eachchapter["id"]==chapter_id) for eachchapter in manifest["classes"][class_index]["chapters"] ]
					if not (True in chapter_already_entered):
						manifest["classes"][class_index]["chapters"].append({ "id": chapter_id, "doclists": [] })
						chapter_already_entered.append(True)
					chapter_index = chapter_already_entered.index(True)

					doclist_already_entered = [ (eachdoclist["id"]==doclist_id) for eachdoclist in manifest["classes"][class_index]["chapters"][chapter_index]["doclists"] ]
					if not (True in doclist_already_entered):
						manifest["classes"][class_index]["chapters"][chapter_index]["doclists"].append({ "id": doclist_id, "documents": [] })
						doclist_already_entered.append(True)
					doclist_index = doclist_already_entered.index(True)

					doc_already_entered = [ (eachdoc==parts[3]) for eachdoc in manifest["classes"][class_index]["chapters"][chapter_index]["doclists"][doclist_index]["documents"] ]
					if not (True in doc_already_entered):
						manifest["classes"][class_index]["chapters"][chapter_index]["doclists"][doclist_index]["documents"].append(parts[3])


	f = open("course_manifest.json", 'w')
	print manifest
	f.write(str(json.dumps(manifest, sort_keys=True)))
	f.close()