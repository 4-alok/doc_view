void main() {
  int pageCount = 857;
  int thread = 4;

  int quotiant = pageCount ~/ thread;

  int start = 0;
  int end = quotiant;

  for (int i = 0; i < thread; i++) {
    print("$start $end");
    start = (end + 1);
    end += quotiant;
  }

  if ((end - quotiant) < pageCount) {
    print("\n${thread * quotiant} $pageCount");
  }
}
