package lcom.sourceModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lcom.utils.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import lcom.InputArgs;

public class SM_Project extends SM_SourceItem {
    private InputArgs inputArgs;
    private List<String> sourceFileList;
    private List<CompilationUnit> compilationUnitList;
    private List<SM_Package> packageList;
    private String unitName;

    public SM_Project(InputArgs argsObj) {
        this.inputArgs = argsObj;
        sourceFileList = new ArrayList<>();
        compilationUnitList = new ArrayList<>();
        packageList = new ArrayList<>();
        setName(this.inputArgs.getProjectName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SM_Package> getPackageList() {
        return packageList;
    }

    private void parseAllPackages() {
        for (SM_Package pkg : packageList) {
            pkg.parse();
        }
    }

    private void createPackageObjects() {
        checkNotNull(compilationUnitList);
        String packageName;
        for (CompilationUnit unit : compilationUnitList) {
            if (unit.getPackage() != null) {
                packageName = unit.getPackage().getName().toString();
            } else {
                packageName = "(default package)";
            }
            SM_Package pkgObj = searchPackage(packageName);
            // If pkgObj is null, package has not yet created
            if (pkgObj == null) {
                pkgObj = new SM_Package(packageName, this, inputArgs);
                packageList.add(pkgObj);
            }
            pkgObj.addCompilationUnit(unit);
        }
    }

    private void checkNotNull(List<CompilationUnit> list) {
        if (list == null) {
            Logger.log("Application couldn't find any source code files in the specified path.");
            System.exit(1);
            Logger.log("Quitting..");
        }
    }

    private SM_Package searchPackage(String packageName) {
        for (SM_Package pkg : packageList) {
            if (pkg.getName().equals(packageName))
                return pkg;
        }
        return null;
    }

    private void createCompilationUnits() {
        try {
            getFileList(inputArgs.getSourceFolder());

            for (String file : sourceFileList) {
                String fileToString = readFileToString(file);
                int startingIndex = file.lastIndexOf(File.separatorChar);
                unitName = file.substring(startingIndex + 1);
                CompilationUnit unit = createAST(fileToString, unitName);
                compilationUnitList.add(unit);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private CompilationUnit createAST(final String content, String unitName) {
        Document doc = new Document(content);
        final ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);
        parser.setUnitName(unitName);
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        parser.setCompilerOptions(options);
        String[] sources = {inputArgs.getSourceFolder()};
        String[] classpaths = new String[inputArgs.getClasspathFolders().size()];
        classpaths = inputArgs.getClasspathFolders().toArray(classpaths);
        parser.setEnvironment(classpaths, sources, null, true);
        parser.setSource(doc.get().toCharArray());

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        if (!cu.getAST().hasBindingsRecovery()) {
            System.out.println("Binding not activated.");
        }
        return cu;
    }

    private void getFileList(String path) {
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null)
            return;
        for (File f : list) {
            if (f.isDirectory()) {
                getFileList(f.getAbsolutePath());
            } else {

                if (f.getName().endsWith(".java"))
                    sourceFileList.add(f.getAbsolutePath());
            }
        }
    }

    private String readFileToString(String sourcePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(sourcePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void parse() {
        Logger.log("Parsing the source code ...");
        createCompilationUnits();
        createPackageObjects();
        parseAllPackages();
    }

    public void resolve() {
        Logger.log("Resolving symbols...");
        for (SM_Package pkg : packageList) {
            pkg.resolve();
        }
    }

    public void computeMetrics() {
        Logger.log("Computing metrics...");
        CSVUtils.initializeCSVDirectory(name, inputArgs.getOutputFolder());
        for (SM_Package pkg : packageList) {
            pkg.extractTypeMetrics();
        }
    }

    public void printSummary() {
    }

    public void exportAnalysisResult() {
        for (SM_Package pkg : packageList) {
            pkg.exportResults();
        }
    }
}
