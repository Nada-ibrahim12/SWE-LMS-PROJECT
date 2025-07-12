package org.example.learning_managment_system;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertNotNull;
 import static org.junit.jupiter.api.Assertions.assertThrows;
 import static org.junit.jupiter.api.Assertions.assertTrue;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
 import static org.mockito.Mockito.verify;
 import static org.mockito.Mockito.when;
 import org.mockito.junit.jupiter.MockitoExtension;

 import com.example.demo.model.Course;
 import com.example.demo.model.Lesson;
 import com.example.demo.model.QuestionBank;
 import com.example.demo.model.user;
 import com.example.demo.repository.CourseRepository;
 import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.QuestionBankRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.CourseService;
import com.example.demo.services.UserService;
 
 @ExtendWith(MockitoExtension.class)
 public class CourseTest {
 
     @Mock
     private CourseRepository courseRepository;
 
     @Mock
     private LessonRepository lessonRepository;
 
     @Mock
     private UserRepository userRepository;
     @Mock
    private QuestionBankRepository questionBankRepository;
 
     @Mock
     private JwtUtil jwtUtil;
 
     @Mock
     private UserService userService;
 
     @InjectMocks
     private CourseService courseService;
 
     private Course testCourse;
     private Lesson testLesson;
     private user testUser;
 
     @BeforeEach
     void setUp() {
         testCourse = new Course();
         testCourse.setId(1L);
         testCourse.setTitle("Test Course");
 
         testLesson = new Lesson();
         testLesson.setTitle("Test Lesson");
 
         testUser = new user("1L", "testStudent", "password123", "Student", "testStudent@example.com");
         testUser.setUserId("1L");
         testUser.setUsername("student");
         testUser.setRole("Student");
     }
     @Test
public void testGetCourseById() {
    Course mockCourse = new Course();
    mockCourse.setId(1L);
    mockCourse.setTitle("Sample Course");

    CourseRepository courseRepository = mock(CourseRepository.class);
    when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));

    Optional<Course> course = courseRepository.findById(1L);
    assertTrue(course.isPresent());
    assertEquals("Sample Course", course.get().getTitle());
}
 
     @Test
     void testCreateCourse_Success() {
         String token = "validToken";
         when(userService.hasRole(token, "Instructor")).thenReturn(true);
         when(courseRepository.save(testCourse)).thenReturn(testCourse);
 
         Course result = courseService.createCourse(testCourse, token);
 
         assertNotNull(result);
         assertEquals(testCourse.getTitle(), result.getTitle());
         verify(courseRepository, times(1)).save(testCourse);
     }
 
     @Test
     void testCreateCourse_Unauthorized() {
         String token = "invalidToken";
         when(userService.hasRole(token, "Instructor")).thenReturn(false);
 
         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             courseService.createCourse(testCourse, token);
         });
 
         assertEquals("Only instructors can create courses", exception.getMessage());
     }
 
     @Test
     void testAddLessonToCourse_Success() {
         String token = "validToken";
         when(userService.hasRole(token, "Instructor")).thenReturn(true);
         when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
         when(courseRepository.save(testCourse)).thenReturn(testCourse);
 
         Course result = courseService.addLessonToCourse(1L, testLesson, token);
 
         assertNotNull(result);
         verify(courseRepository, times(1)).save(testCourse);
     }
 
     @Test
     void testAddLessonToCourse_CourseNotFound() {
         String token = "validToken";
         when(userService.hasRole(token, "Instructor")).thenReturn(true);
         when(courseRepository.findById(1L)).thenReturn(Optional.empty());
 
         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             courseService.addLessonToCourse(1L, testLesson, token);
         });
 
         assertEquals("Course not found", exception.getMessage());
     }
 
     @Test
     void testViewAvailableCourses_Success() {
         String token = "validToken";
         String username = "student";
         when(jwtUtil.extractUsername(token)).thenReturn(username);
         when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
         when(courseRepository.findAll()).thenReturn(List.of(testCourse));
 
         List<Course> courses = courseService.viewAvailableCourses(token);
 
         assertNotNull(courses);
         assertEquals(1, courses.size());
         verify(courseRepository, times(1)).findAll();
     }
 
     @Test
     void testViewAvailableCourses_Unauthorized() {
         String token = "validToken";
         testUser.setRole("Instructor"); 
         String username = "instructor";
         when(jwtUtil.extractUsername(token)).thenReturn(username);
         when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
 
         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             courseService.viewAvailableCourses(token);
         });
 
         assertEquals("Only students can view available courses.", exception.getMessage());
     }
 
     @Test
void testCreateQuestionBank_Success() {
    String token = "validToken";
    QuestionBank questionBank = new QuestionBank();
    questionBank.setName("Sample Question Bank");

    when(userService.hasRole(token, "Instructor")).thenReturn(true);
    when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
    when(questionBankRepository.save(any(QuestionBank.class))).thenAnswer(invocation -> {
        QuestionBank savedQuestionBank = invocation.getArgument(0);
        savedQuestionBank.setId(1L); // Simulate DB-generated ID
        return savedQuestionBank;
    });

    QuestionBank result = courseService.createQuestionBank(1L, questionBank, token);

    assertNotNull(result); 
    assertEquals(testCourse, result.getCourse()); 
    verify(courseRepository, times(1)).findById(1L);
    verify(questionBankRepository, times(1)).save(questionBank);
}

 
     @Test
    void testAddMediaToCourse_Success() {
    String fileUrl = "http://example.com/media.pdf";

    testCourse = new Course();
    testCourse.setMediaFiles(new ArrayList<>());

    when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
    when(courseRepository.save(testCourse)).thenReturn(testCourse);

    courseService.addMediaToCourse(1L, fileUrl);

    assertTrue(testCourse.getMediaFiles().contains(fileUrl));
    verify(courseRepository, times(1)).save(testCourse);
}
 
     @Test
     void testAddMediaToCourse_CourseNotFound() {
         String fileUrl = "http://example.com/media.pdf";
         when(courseRepository.findById(1L)).thenReturn(Optional.empty());
 
         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             courseService.addMediaToCourse(1L, fileUrl);
         });
 
         assertEquals("Course not found", exception.getMessage());
     }
 }
 