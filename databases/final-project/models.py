from flask import Flask
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)

# Models
class College(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(2048), unique=True)
    state_abbr = db.Column(db.String(16))
    lat = db.Column(db.Float())
    lon = db.Column(db.Float())
    student_expense = db.relationship('StudentExpense', backref='college', 
        uselist=False)
    degree_salaries = db.relationship('CollegeDegreeSalary', backref='college')

    def __init__(self, name, state_abbr, lat, lon):
        self.name = name
        self.state_abbr = state_abbr
        self.lat = lat
        self.lon = lon

    def __repr__(self):
        return '\nname: {}, state_abbr: {}, lat: {}, lon: {}\n'.format(self.name, 
            self.state_abbr, self.lat, self.lon)

class StudentExpense(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    college_id = db.Column(db.Integer, db.ForeignKey('college.id'), unique=True)
    in_state_tuition = db.Column(db.Integer)
    out_state_tuition = db.Column(db.Integer)
    book_supplies = db.Column(db.Integer)
    on_campus_room_board = db.Column(db.Integer)
    off_campus_room_board = db.Column(db.Integer)
    average_aid = db.Column(db.Integer)

    def __init__(self, college_id, in_state_tuition, out_state_tuition, \
    book_supplies, on_campus_room_board, off_campus_room_board, average_aid):
        self.college_id = college_id
        self.in_state_tuition = in_state_tuition
        self.out_state_tuition = out_state_tuition
        self.book_supplies = book_supplies
        self.on_campus_room_board = on_campus_room_board
        self.off_campus_room_board = off_campus_room_board
        self.average_aid = average_aid

    def __repr__(self):
        return 'Too lazy to write repr for StudentExpense'

class CollegeExpense(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    college_id = db.Column(db.Integer, db.ForeignKey('college.id'), unique=True)
    instruction = db.Column(db.Integer)
    research = db.Column(db.Integer)
    public_service = db.Column(db.Integer)
    academic_support = db.Column(db.Integer)
    other = db.Column(db.Integer)

    def __init__(self, college_id, instruction, research, public_service, \
    academic_support, other):
        self.college_id = college_id
        self.instruction = instruction,
        self.research = research
        self.public_service = public_service
        self.academic_support = academic_support
        self.other = other

    def __repr__(self):
        return 'Too lazy to write repr for CollegeExpense'

class Degree(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(1024), unique=True)
    college_salaries = db.relationship('CollegeDegreeSalary', backref='degree')

    def __init__(self, name):
        self.name = name

    def __repr__(self):
        return '\nname: {}\n'.format(self.name)

class CollegeDegreeSalary(db.Model):
    __table_args__ = (db.UniqueConstraint('degree_id', 'college_id', name='college_payoff'),)

    id = db.Column(db.Integer, primary_key=True)
    degree_id = db.Column(db.Integer, db.ForeignKey('degree.id'))
    college_id = db.Column(db.Integer, db.ForeignKey('college.id'))
    salary = db.Column(db.Integer)

    def __init__(self, degree_id, college_id, salary):
        self.degree_id = degree_id
        self.college_id = college_id
        self.salary = salary

    def __repr__(self):
        return '\ndegree_id: {}, college_id: {}, salary: {}\n'.format(self.degree_id, 
            self.college_id, self.salary)
