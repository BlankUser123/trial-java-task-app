package com.k1ts.check.taskcheckservice.year_2024.course_2.oop.practice_1;

import com.k1ts.check.CheckResult;
import com.k1ts.check.codeinspector.CodeInspector;
import com.k1ts.check.codevalidator.CodeValidator;
import com.k1ts.check.javacodecontainer.TestJavaCodeContainer;
import com.k1ts.check.taskcheckservice.DefaultAbstractTaskCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service("year-2024-course-2-oop-practice-1-task-1")
public class Year2024Course2OopPractice1Task1CheckService extends DefaultAbstractTaskCheckService {

    @Lookup
    public CodeInspector getCodeInspector() {
        return null;
    }

    @Override
    public CheckResult doCheck(String code) {
        CodeInspector codeInspector = getCodeInspector();

        codeInspector.setClassName("JavaIsFunClass");

        codeInspector.addCodeValidator((checkResult) -> CodeValidator
                .builder()
                .className("JavaIsFunClass")
                .code(code)
                .methodValidator(CodeValidator.MethodValidator
                        .builder()
                        .methodName("public static void main(String[] args)")
                        .line(CodeValidator.LineValidator
                                .builder()
                                .line("System\\.out\\.println\\(.*\\);")
                                .successfulMassageIfLinePresent("Ти використав метод println")
                                .failedMessageIfLineNotPresent("Ти не використав метод println")
                                .build())
                        .build())
                .build()
                .validate(checkResult));

        codeInspector.addJavaContainer(TestJavaCodeContainer
                .builder()
                .className("TestCase")
                .javaCode("""
                        public class TestCase {
                            public static void main(String[] args) {
                                JavaIsFunClass.main(args);
                            }
                        }
                        """)
                .build());

        codeInspector.addPostProcessor((checkResult, codeResult) -> {
            Map<String, String> classConsoleOutputMap = codeResult.getClassConsoleOutputMap();

            checkResult.setConsoleOutput(classConsoleOutputMap.get("JavaIsFunClass"));

            String actualOutput = classConsoleOutputMap.get("TestCase").strip();

            String expectedOutput = "Java — це весело!";

            if (expectedOutput.equals(actualOutput)) {
                checkResult.getSuccessResult().add("Твій код працює вірно");
            } else {
                checkResult.getFailedResult().add("Твій код працює невірно, повинен виводити " + expectedOutput + ", а виводить " + actualOutput);
            }
        });

        return codeInspector.inspect(code);
    }

    private static class JavaIsFunClass {
        public static void main(String[] args) {
            System.out.println("Java — це весело!");
        }
    }
}
