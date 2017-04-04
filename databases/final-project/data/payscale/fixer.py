from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from models import College, Degree, CollegeDegreeSalary
from random import randint

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)

def rand_salary(degree_id):
    salaries = [row.salary for row in 
        CollegeDegreeSalary.query.filter_by(degree_id=degree_id).all()]
    if not salaries:
        return randint(55000, 90000)

    min_salary = min(salaries)
    max_salary = max(salaries)

    return randint(min_salary, max_salary)

def start():
    for college in College.query.all():
        for degree in Degree.query.all():
            try:
                cds = CollegeDegreeSalary(degree.id, college.id, rand_salary(degree.id))
                db.session.add(cds)
                db.session.commit()
                print 'added: {}'.format(cds)
            except Exception as e:
                # we good fam
                print 'already there'
                print '{}'.format(e)
                db.session.rollback()
                pass

if __name__ == '__main__':
    start()
