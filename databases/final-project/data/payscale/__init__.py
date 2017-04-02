from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from models import College, Degree, CollegeDegreeSalary
from bs4 import BeautifulSoup
from urllib2 import urlopen
from fuzzywuzzy import fuzz

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)

ENTRY_POINT_FILENAME = 'entry.html'
BASE_URL = 'http://www.payscale.com'

def find_college_id(college_name):
    for college in College.query.all():
        if fuzz.ratio(college.name, college_name) > 80:
            print 'got a match: {} and {}'.format(college.name, college_name)
            return college.id

    return -1

def start():
    with open(ENTRY_POINT_FILENAME, 'r') as entry_point_file:
        table = BeautifulSoup(entry_point_file, 'lxml')
        links = table.find_all('a')
        
        for link in links:
            href = link['href']
            href += '/by_Degree'
            college_page_link = BASE_URL + href
            college_page_html = urlopen(college_page_link).read()
            college_page_soup = BeautifulSoup(college_page_html, 'lxml')
            college = href.split('/')[3].replace('School=', '')

            for table in college_page_soup.find_all('table'):
                if not (table.has_attr('class') and 'tlf' in table['class'] \
                and 'f11' in table['class'] and 'w585' in table['class']):
                    continue

                rows = table.find_all('tr')
                for row in rows:
                    if row.has_attr('class') or not row.th or not row.td:
                        continue
                    
                    # man im tired
                    try:
                        degree = row.th.a.string
                        salary = row.td.string
                    except:
                        continue

                    # may be in a range '$x - $y' so take average
                    # also remove $
                    tokens = salary.split()
                    if len(tokens) == 3:
                        min = int(tokens[0][1:].replace(',', ''))
                        max = int(tokens[2][1:].replace(',', ''))
                        salary = (min + max) / 2
                    else:
                        salary = int(tokens[0][1:].replace(',', ''))
                    salary = int(salary)

                    # make db insertions
                    degree_id = -1
                    college_id = -1
                    try:
                        degree = Degree(degree)
                        db.session.add(degree)
                        db.session.commit()
                        degree_id = degree.id
                    except:
                        db.session.rollback()
                        degree = Degree.query.filter_by(name=degree.name).first()
                        if not degree:
                            continue
                        degree_id = degree.id
                    
                    college_id = find_college_id(college)
                    if college_id == -1:
                        continue
    
                    try:
                        college_degree_salary = CollegeDegreeSalary(degree_id, college_id, salary)
                        db.session.add(college_degree_salary)
                        db.session.commit()
                    except:
                        continue

if __name__ == '__main__':
    start()
