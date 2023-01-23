package mk.ukim.finki.wp.kol2022.g2.service.Impl;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.model.StudentType;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidCourseIdException;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidStudentIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.CourseRepository;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import mk.ukim.finki.wp.kol2022.g2.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Student> listAll() {
        return this.studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return this.studentRepository.findById(id).orElseThrow(InvalidCourseIdException::new);
    }

    @Override
    public Student create(String name, String email, String password, StudentType type, List<Long> courseId, LocalDate enrollmentDate) {
        List<Course> courses= this.courseRepository.findAllById(courseId);
        if(courses.isEmpty()) throw new InvalidCourseIdException();
        String pass= passwordEncoder.encode(password);

        Student student= new Student(name, email, pass, type, courses, enrollmentDate);
        return this.studentRepository.save(student);
    }

    @Override
    public Student update(Long id, String name, String email, String password, StudentType type, List<Long> coursesId, LocalDate enrollmentDate) {
        Student student= this.studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
        List<Course> courses= this.courseRepository.findAllById(coursesId);
        if(courses.isEmpty()) throw new InvalidCourseIdException();

        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);
        student.setType(type);
        student.setCourses(courses);
        student.setEnrollmentDate(enrollmentDate);

        return this.studentRepository.save(student);
    }

    @Override
    public Student delete(Long id) {
        Student student= this.studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
        studentRepository.delete(student);
        return student;
    }

    @Override
    public List<Student> filter(Long courseId, Integer yearsOfStudying) {
        return null;
    }
}
