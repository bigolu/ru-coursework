BSIZE=256
BCOUNT=8
INPUT="../test_graphs/msdoor.txt"
OUTPUT="output.txt"
METHOD="bmf"
USEMEM="no"
SYNC="incore"

ARGS="--input $INPUT --bsize $BSIZE --bcount $BCOUNT --output $OUTPUT --method $METHOD --usemem $USEMEM --sync $SYNC"

./sssp $ARGS
