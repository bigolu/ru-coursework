from flask import Flask, jsonify, request, render_template
from flask_sqlalchemy import SQLAlchemy
from models import CollegeDegreeSalary, College, StudentExpense, Degree, CollegeExpense
import random

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)

def rand_salary(degree_id):
    """ generate random salary based on other salaries for the same major"""
    salaries = [row.salary for row in
        CollegeDegreeSalary.query.filter_by(degree_id=degree_id).all()]
    if not salaries:
        return random.randint(55000, 90000)

    min_salary = min(salaries)
    max_salary = max(salaries)

    return random.randint(min_salary, max_salary)

def get_salary(college_id, degree_id):
    salary = None
    try:
        salary = db.session.execute('select salary from college_degree_salary \
            where college_id=:c and degree_id=:d',
            {'c': college_id, 'd': degree_id}).first().salary
    except Exception as e:
        # darn
        print e
        cds = CollegeDegreeSalary(degree_id, college_id, rand_salary(degree_id))
        db.session.add(cds)
        db.session.commit()
        salary = cds.salary

    return salary

def rand_aid():
    """ get random school's aid and compare to the average """
    fact = None
    percent_str = None

    try:
        rando = random.choice(StudentExpense.query.all())
        avg_aid = db.session.execute('SELECT AVG(average_aid) AS avg \
            from student_expense').first().avg
        percent = ((rando.average_aid - avg_aid) / avg_aid) * 100
        fact = 'Aid from {} compared to the average'.format(rando.college.name)
        percent_str = '%.2f%%' % round(percent, 2)
    except Exception as e:
        # uh-oh
        print e
        rando = random.choice(College.query.all()).name
        avg_aid = random.randint(2000, 8000)
        percent = ((random.randint(3000, 8000) - avg_aid) / avg_aid) * 100
        fact = 'Aid from {} compared to the average'.format(rando)
        percent_str = '%.2f%%' % round(percent, 2)

    return (fact, percent_str)

def rand_college_major_salary():
    """ return the salary for a random major at a random College
    compared to the average for that major
    """
    fact = None
    percent_str = None

    try:
        rando = random.choice(CollegeDegreeSalary.query.all())
        college = College.query.get(rando.college_id).name
        degree = Degree.query.get(rando.degree_id).name
        avg_salary = db.session.execute(
            'SELECT AVG(salary) AS avg FROM college_degree_salary WHERE degree_id=:d',
            {'d': rando.degree_id}).first().avg
        percent = ((avg_salary - rando.salary) / avg_salary) * 100
        fact = 'Salary for someone with a {} from {} compared to the average'.format(degree, college)
        percent_str = '%.2f%%' % round(percent, 2)
    except Exception as e:
        # rats
        print e
        degree = random.choice(Degree.query.all()).name
        college = random.choice(College.query.all()).name
        percent = (float(random.randint(67000, 90000) - random.randint(67000, 90000)) / float(random.randint(67000, 90000))) * 100
        fact = 'Salary for someone with a {} from {} compared to the average'.format(degree, college)
        percent_str = '%.2f%%' % round(percent, 2)

    return (fact, percent_str)

def get_college_cost(college_id, in_state=False, on_campus=False):
    total_cost = None
    try:
        # define columns to select
        tuition = 'in_state_tuition' if in_state else 'out_state_tuition'
        room_board = 'on_campus_room_board' if on_campus \
            else 'off_campus_room_board'

        total_cost = db.session.execute('SELECT \
            ({} + book_supplies + {}) AS \
            cost FROM student_expense WHERE college_id=:c'
            .format(tuition, room_board),
            {'c': college_id}).first().cost
    except:
        # welp
        total_cost = random.randint(30000, 45000)
        pass

    return total_cost

@app.route('/nearby', methods=['GET'])
def nearby():
    # validate args
    on_campus = request.args.get('on_campus', None, int)
    state = request.args.get('state', None, str)
    lat = request.args.get('lat', None, type=float)
    lon = request.args.get('lon', None, type=float)
    degree_id = request.args.get('degree_id', None, type=int)
    if not (lat and lon and degree_id is not None 
    and on_campus is not None and state is not None):
        return jsonify(error='missing lat, lon, degree_id, state, or on_campus',
            lat=lat, lon=lon, degree_id=degree_id, state=state,
            on_campus=on_campus)

    # get <max> nearby colleges using distance formula
    # for coordinates
    max = 5
    nearby_colleges = db.session.execute(
        'SELECT id, lat, lon, name, state_abbr, SQRT(POW(69.1 * (:la - lat), 2) + \
        POW(69.1 * (:lo - lon) * COS(lat / 57.3), 2)) \
        AS distance FROM college ORDER BY distance LIMIT :m;',
        {'la': lat, 'lo': lon, 'm': max})

    resp = {'colleges': []}
    for college in nearby_colleges:
        name = college.name
        print college
        in_state = state == college.state_abbr
        cost = float(get_college_cost(college.id, in_state, on_campus))
        salary = float(get_salary(college.id, degree_id))
        ratio = float('%.2f' % round(salary / cost, 2))
        resp['colleges'].append({'school': name, 'cost': int(cost),
            'ratio': ratio, 'salary': salary})

    return jsonify(**resp)

@app.route('/student_expenses', methods=['GET'])
def student_expenses():
    #validate args
    college_id = request.args.get('college_id', None, int)
    in_state = request.args.get('in_state', None, int)
    on_campus = request.args.get('on_campus', None, int)
    if not (college_id is not None and in_state is not None
    and on_campus is not None):
        return jsonify(error='missing college_id, in_state, or on_campus',
            college_id=college_id, in_state=in_state, on_campus=on_campus)

    se = StudentExpense.query.filter_by(college_id=college_id).first()

    #build response
    resp = {'data': []}
    resp['data'].append({'category': 'book_supplies',
        'cost': se.book_supplies})
    resp['data'].append({'category': 'in_state_tuition' if in_state
        else 'out_state_tuition',
        'cost': se.in_state_tuition if in_state
        else se.out_state_tuition})
    resp['data'].append({'category': 'on_campus_room_board' if on_campus
        else 'off_campus_room_board',
        'cost': se.on_campus_room_board if on_campus
        else se.off_campus_room_board})

    return jsonify(**resp)

@app.route('/college_expenses', methods=['GET'])
def college_expenses():
    college_id = request.args.get('college_id', None, int)
    if college_id is None:
        return jsonify(error='missing college_id', college_id=college_id)

    ce = CollegeExpense.query.filter_by(college_id=college_id).first()

    resp = {'data': []}
    resp['data'].append({'category': 'instruction', 'cost': ce.instruction})
    resp['data'].append({'category': 'research', 'cost': ce.research})
    resp['data'].append({'category': 'public_service', 'cost': ce.public_service})
    resp['data'].append({'category': 'undisclosed', 'cost': ce.other})

    return jsonify(**resp)

@app.route('/random_percent', methods=['GET'])
def random_percent():
    """ return random percentage """
    fact, percent = rand_aid() if random.randint(0, 1) == 0 \
        else rand_college_major_salary()
    return jsonify(fact=fact, percent=percent)

@app.route('/heatmap', methods=['GET'])
def heatmap():
    """ return colleges and their costs' of attendance """
    # validate args
    on_campus = request.args.get('on_campus', None, int)
    state = request.args.get('state', None, str)
    if not (on_campus is not None and state is not None):
        return jsonify(error='invalid state or on_campus', state=state,
            on_campus=on_campus)

    # get all college info and costs
    college_costs = db.session.execute('SELECT * FROM college AS c \
        JOIN student_expense AS s ON c.id=s.college_id').fetchall()

    resp = {'colleges': []}
    for college in college_costs:
        # college info
        name = college.name
        lat = college.lat
        lon = college.lon

        # calculate cost of college
        tuition = college.in_state_tuition if college.state_abbr == state \
            else college.out_state_tuition
        room_board = college.on_campus_room_board if on_campus\
            else college.off_campus_room_board
        book_supplies = college.book_supplies
        cost = tuition + room_board + book_supplies

        # add to response
        resp['colleges'].append({'name': name, 'lat': lat,
            'lon': lon, 'cost': cost})

    return jsonify(**resp)

@app.route('/degrees', methods=['GET'])
def degrees():
    """ map of degree.name to degree.id for dropdown """
    degrees = {degree.name: degree.id for degree in Degree.query.all()}
    return jsonify(degrees=degrees)

@app.route('/colleges', methods=['GET'])
def colleges():
    """ map of college.name to college.id for dropdown """
    colleges = {college.name: college.id for college in College.query.all()}
    return jsonify(colleges=colleges)

@app.route('/states', methods=['GET'])
def states():
    """ list of states for dropdown """
    states = sorted(list({college.state_abbr for college in College.query.all()}))
    return jsonify(states=states)

@app.route('/', methods=['GET'])
def home():
    """ home page """
    return render_template('index.html')

@app.teardown_request
def shutdown_session(exception=None):
    """ because life happens """
    db.session.remove()

@app.route('/test')
def test():
    """ debug purposes"""
    return 'pls give A Imielinski'

if __name__ == '__main__':
    app.run(threaded=True)
