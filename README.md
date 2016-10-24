# differ

Finding new entries in address lists. Show how they differ from the previous list and the new list.

## Usage

In the sample-data directory are two example fiels. The prev.csv is what we have and the next.csv is the new file. 
Using diff we want to know which names are new in the next.csv file.

- java -jar target/differ-1.0.0-SNAPSHOT-standalone.jar sample-data/prev.csv sample-data/next.csv

## License

Copyright (C) 2011 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
