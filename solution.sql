WITH emp_paid AS (
  SELECT DISTINCT e.emp_id, e.first_name, e.last_name, e.dob, e.department
  FROM employee e
  JOIN payments p ON p.emp_id = e.emp_id
  WHERE p.amount > 70000
),
emp_age AS (
  SELECT ep.*,
         FLOOR(DATEDIFF(CURRENT_DATE, ep.dob) / 365.25) AS age
  FROM emp_paid ep
),
ranked AS (
  SELECT ea.*,
         ROW_NUMBER() OVER (PARTITION BY ea.department ORDER BY ea.last_name, ea.first_name) AS rn
  FROM emp_age ea
)
SELECT d.department_name AS DEPARTMENT_NAME,
       ROUND(AVG(r.age), 2) AS AVERAGE_AGE,
       GROUP_CONCAT(
         CASE WHEN r.rn <= 10 THEN CONCAT(r.first_name, ' ', r.last_name) END
         ORDER BY r.rn SEPARATOR ', '
       ) AS EMPLOYEE_LIST
FROM ranked r
JOIN department d ON d.department_id = r.department
GROUP BY d.department_id, d.department_name
ORDER BY d.department_id DESC;
