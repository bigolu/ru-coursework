BSIZE=32
BCOUNT=32
INPUT="test_graphs/roadNet-CA.txt"
OUTPUT="output.txt"
METHOD="tpe"
USEMEM="no"
SYNC="incore"

ARGS="--input $INPUT --bsize $BSIZE --bcount $BCOUNT --output $OUTPUT --method $METHOD --usemem $USEMEM --sync $SYNC"

./sssp $ARGS
