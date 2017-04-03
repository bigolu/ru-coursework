BSIZE=512
BCOUNT=100
INPUT="test_graphs/roadNet-CA.txt"
OUTPUT="output.txt"
METHOD="bmf"
USEMEM="no"
SYNC="incore"

ARGS="--input $INPUT --bsize $BSIZE --bcount $BCOUNT --output $OUTPUT --method $METHOD --usemem $USEMEM --sync $SYNC"

./sssp $ARGS
