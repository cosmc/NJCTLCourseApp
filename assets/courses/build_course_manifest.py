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
	# I mean, egads.
	for filename in allfiles:
		parts = filename.split("/")[1:]
		if not (".DS_Store" in parts):
			if len(parts) >= 4:

				class_already_entered = [ (eachclass["title"]==parts[0]) for eachclass in manifest["classes"] ]
				if not (True in class_already_entered):
					manifest["classes"].append({ "title": parts[0], "chapters": [] })
					class_already_entered.append(True)

				chapter_already_entered = [ (eachchapter["title"]==parts[1]) for eachchapter in manifest["classes"][class_already_entered.index(True)]["chapters"] ]
				if not (True in chapter_already_entered):
					manifest["classes"][class_already_entered.index(True)]["chapters"].append({ "title": parts[1], "doclists": [] })
					chapter_already_entered.append(True)

				doclist_already_entered = [ (eachdoclist["title"]==parts[2]) for eachdoclist in manifest["classes"][class_already_entered.index(True)]["chapters"][chapter_already_entered.index(True)]["doclists"] ]
				if not (True in doclist_already_entered):
					manifest["classes"][class_already_entered.index(True)]["chapters"][chapter_already_entered.index(True)]["doclists"].append({ "title": parts[2], "documents": [] })
					doclist_already_entered.append(True)

				doc_already_entered = [ (eachdoc==parts[3]) for eachdoc in manifest["classes"][class_already_entered.index(True)]["chapters"][chapter_already_entered.index(True)]["doclists"][doclist_already_entered.index(True)]["documents"] ]
				if not (True in doc_already_entered):
					manifest["classes"][class_already_entered.index(True)]["chapters"][chapter_already_entered.index(True)]["doclists"][doclist_already_entered.index(True)]["documents"].append(parts[3])


	f = open("course_manifest.json", 'w')
	print manifest
	f.write(str(json.dumps(manifest, sort_keys=True)))
	f.close()