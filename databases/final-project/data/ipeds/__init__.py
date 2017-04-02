"""
Populate the college, student_expense, and college_expense tables with data from a csv from 
http://nces.ed.gov/ipeds/datacenter/Default.aspx
"""
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from models import College, StudentExpense, CollegeExpense
import csv
from random import randint

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)

# start enum
NAME = 1
STATE = 3
LON = 4
LAT = 5
IN_STATE = 6
OUT_STATE = 7
BOOK = 8
ON_CAMPUS = 9
OFF_CAMPUS = 10
AID = 11
INSTRUCTION = 12
RESEARCH = 13
PUBLIC = 14
ACADEMIC = 15
# end enum

def populate_tables(data):
    # skip column names
    next(data) 

    for row in data:
        name = row[NAME]
        state = row[STATE]
        lon = row[LON]
        lat = row[LAT]
        in_state = row[IN_STATE]
        out_state = row[OUT_STATE]
        book = row[BOOK]
        on_campus = row[ON_CAMPUS]
        off_campus = row[OFF_CAMPUS]
        aid = row[AID]
        instruction = row[INSTRUCTION]
        research = row[RESEARCH]
        public = row[PUBLIC]
        academic = row[ACADEMIC]
        other = -1

        # must haves
        if not (name and state and lon and lat and (in_state or out_state) 
        and book and (on_campus or off_campus)):
            print 'missing crucial column for row: {}'.format(row)
            continue

        # fix tuition
        in_state = in_state or (int(out_state) - 5325)
        out_state = out_state or (int(in_state) + 6382)

        # fix housing
        on_campus = on_campus or (int(off_campus) + 1356)
        off_campus = off_campus or (int(on_campus) - 1456)

        # fix aid
        aid = aid or randint(2000, 10000)

        # fix expenditures
        instruction = instruction or randint(1, 10)
        research = research or randint(1, 10)
        public = public or randint(1, 10)
        academic = academic or randint(1, 10)

        # format
        name = str(name)
        state = str(state)
        lon = float(lon)
        lat = float(lat)
        in_state = int(in_state)
        out_state = int(out_state)
        book = int(book)
        on_campus = int(on_campus)
        aid = int(aid)
        instruction = int(instruction)
        research = int(research)
        public = int(public)
        academic = int(academic)
        other = 100 - (instruction + research + public + academic)

        try:
            # add to college
            college = College(name, state, lat, lon)
            db.session.add(college)
            db.session.commit()
        
            # necessary for foreign key
            college_id = college.id

            # add to student_expense
            student_expense = StudentExpense(college_id, in_state, out_state, 
            book, on_campus, off_campus, aid)
            db.session.add(student_expense)
            db.session.commit()

            # add to college_expense
            college_expense = CollegeExpense(college_id, instruction, research, 
            public, academic, other)
            db.session.add(college_expense)
            db.session.commit()
        except Exception as e:
            print e
            db.session.rollback()

def start():
    with open('data.csv') as datafile:
        data = csv.reader(datafile, delimiter=',', quotechar='|')
        populate_tables(data)

if __name__ == '__main__':
    start()
