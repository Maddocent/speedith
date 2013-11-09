package speedith.core.reasoning.util.unitary;

import speedith.core.lang.*;
import speedith.core.lang.reader.ReadingException;
import speedith.core.lang.reader.SpiderDiagramsReader;
import speedith.core.reasoning.GoalsTest;

import java.io.IOException;
import java.util.*;

import static java.util.Collections.unmodifiableList;
import static speedith.core.lang.SpiderDiagrams.createPrimarySD;
import static speedith.core.lang.Zones.getZonesInsideAllContours;
import static speedith.core.lang.Zones.getZonesOutsideContours;

public class TestSpiderDiagrams {
    public static final List<Zone> POWER_REGION_ABCD = unmodifiableList(Zones.allZonesForContours("A", "B", "C", "D"));
    public static final List<Zone> POWER_REGION_ABC = unmodifiableList(Zones.allZonesForContours("A", "B", "C"));
    private static final String[] VALID_SPIDER_DIAGRAM_SDT_FILES = {
            "ParserExample1.sd",
            "ParserExample2.sd",
            "ParserExample3.sd",
            "ParserExample4.sd",
            "ParserExample5.sd"
    };
    public static final PrimarySpiderDiagram diagramSpeedithPaperD1 = getDiagramSpeedithPaperD1();
    public static final PrimarySpiderDiagram diagramSpeedithPaperD2 = getDiagramSpeedithPaperD2();

    public static PrimarySpiderDiagram getDiagramD1PrimeFromSpeedithPaper() {
        SortedSet<Zone> initialPresentZones = diagramSpeedithPaperD1.getPresentZones();
        ArrayList<Zone> presentZones = Zones.sameRegionWithNewContours(initialPresentZones, "E");
        presentZones.removeAll(Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(presentZones, "A"), "E"));
        presentZones.remove(Zone.fromInContours("A", "C").withOutContours("B", "D", "E"));

        ArrayList<Zone> vennABCDEZones = Zones.allZonesForContours("A", "B", "C", "D", "E");
        TreeSet<Zone> shadedZones = new TreeSet<>();
        shadedZones.addAll(Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(vennABCDEZones, "E"), "C"));
        shadedZones.addAll(Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(vennABCDEZones, "A"), "E"));
        shadedZones.addAll(Zones.getZonesInsideAllContours(vennABCDEZones, "A", "B"));
        shadedZones.addAll(Zones.getZonesInsideAllContours(vennABCDEZones, "B", "C"));
        shadedZones.addAll(Zones.getZonesInsideAllContours(vennABCDEZones, "C", "D"));

        TreeSet<Zone> spider2Region = new TreeSet<>();
        spider2Region.addAll(Zones.getZonesInsideAllContours(presentZones, "B"));
        spider2Region.add(Zone.fromOutContours("A", "B", "C", "D", "E"));
        spider2Region.add(Zone.fromOutContours("A", "B", "C", "E").withInContours("D"));

        Map<String, Region> habitats = new TreeMap<>();
        habitats.put("s1", new Region(Zones.getZonesInsideAllContours(presentZones, "C")));
        habitats.put("s2", new Region(spider2Region));

        return SpiderDiagrams.createPrimarySD(habitats.keySet(), habitats, shadedZones, presentZones);
    }

    public static PrimarySpiderDiagram getDiagramSpeedithPaperD2() {
        return getDiagramSpeedithPaperD2("A", "E");
    }

    public static PrimarySpiderDiagram getDiagramSpeedithPaperD2(String outsideContour, String insideContour) {
        String contourC = "C";
        String contourF = "F";
        ArrayList<Zone> power4Region = Zones.allZonesForContours(outsideContour, contourF, contourC, insideContour);
        ArrayList<Zone> shaded_E_A = Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(power4Region, outsideContour), insideContour);
        ArrayList<Zone> shaded_C = Zones.getZonesInsideAllContours(power4Region, contourC);
        ArrayList<Zone> shaded_F_ACE = Zones.getZonesInsideAnyContour(Zones.getZonesInsideAllContours(power4Region, contourF), outsideContour, contourC, insideContour);

        TreeSet<Zone> presentZones = new TreeSet<>(power4Region);
        presentZones.removeAll(shaded_E_A);
        presentZones.removeAll(shaded_C);
        presentZones.removeAll(shaded_F_ACE);

        TreeSet<Zone> shadedZones = new TreeSet<>();
        shadedZones.addAll(shaded_E_A);
        shadedZones.addAll(shaded_C);
        shadedZones.addAll(shaded_F_ACE);

        TreeMap<String, Region> habitats = new TreeMap<>();
        habitats.put("s1", new Region(Zone.fromInContours(outsideContour, insideContour, contourC).withOutContours(contourF)));
        habitats.put("s2", new Region(
                Zone.fromInContours(contourF).withOutContours(outsideContour, contourC, insideContour),
                Zone.fromOutContours(outsideContour, contourC, insideContour, contourF)
        ));
        habitats.put("s3", new Region(
                Zone.fromInContours(contourF).withOutContours(outsideContour, contourC, insideContour),
                Zone.fromOutContours(outsideContour, contourC, insideContour, contourF)
        ));

        return SpiderDiagrams.createPrimarySD(habitats.keySet(), habitats, shadedZones, presentZones);
    }

    public static PrimarySpiderDiagram getDiagramSpeedithPaperD1() {
        ArrayList<Zone> shaded_C_A = Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(POWER_REGION_ABCD, "A"), "C");
        ArrayList<Zone> shaded_CBD = Zones.getZonesInsideAnyContour(Zones.getZonesInsideAnyContour(POWER_REGION_ABCD, "B", "D"), "C");
        ArrayList<Zone> shaded_AB = Zones.getZonesInsideAllContours(POWER_REGION_ABCD, "A", "B");

        TreeSet<Zone> presentZones = new TreeSet<>(POWER_REGION_ABCD);
        presentZones.removeAll(shaded_C_A);
        presentZones.removeAll(shaded_CBD);
        presentZones.removeAll(shaded_AB);
        presentZones.removeAll(shaded_AB);

        TreeSet<Zone> shadedZones = new TreeSet<>(shaded_C_A);
        shadedZones.addAll(shaded_CBD);
        shadedZones.addAll(shaded_AB);

        TreeMap<String, Region> habitats = new TreeMap<>();
        habitats.put("s1", new Region(Zone.fromInContours("A", "C").withOutContours("B", "D")));
        habitats.put("s2", new Region(
                Zone.fromInContours("B", "D").withOutContours("A", "C"),
                Zone.fromInContours("D").withOutContours("B", "A", "C"),
                Zone.fromInContours("B").withOutContours("D", "A", "C"),
                Zone.fromOutContours("B", "D", "A", "C")
        ));

        return SpiderDiagrams.createPrimarySD(habitats.keySet(), habitats, shadedZones, presentZones);
    }

    public static PrimarySpiderDiagram getDiagramFromSpeedithPaper_Fig7_1st() {
        return (PrimarySpiderDiagram) tryReadSpiderDiagramFromSDTFile(3).getSubDiagramAt(2);
    }

    public static PrimarySpiderDiagram getDiagramFromSpeedithPaper_Fig7_2nd() {
        ArrayList<Zone> shadedZones = new ArrayList<>();
        ArrayList<Zone> powerRegionCD = Zones.allZonesForContours("C", "D");
        shadedZones.addAll(getZonesOutsideContours(getZonesInsideAllContours(powerRegionCD, "C"), "D"));
        shadedZones.addAll(getZonesInsideAllContours(getZonesOutsideContours(powerRegionCD, "C"), "D"));

        ArrayList<Zone> presentZones = new ArrayList<>(powerRegionCD);
        presentZones.removeAll(shadedZones);

        TreeMap<String, Region> habitats = new TreeMap<>();
        habitats.put("s2", new Region(
                Zone.fromInContours("C", "D"),
                Zone.fromInContours("C").withOutContours("D")
        ));
        habitats.put("s1", new Region(
                Zone.fromOutContours("C", "D")
        ));

        return createPrimarySD(habitats.keySet(), habitats, shadedZones, presentZones);
    }

    public static PrimarySpiderDiagram getDiagramFromSpeedithPaper_Fig7_3rd() {
        ArrayList<Zone> shadedZones = new ArrayList<>();
        shadedZones.addAll(getZonesInsideAllContours(POWER_REGION_ABCD, "A", "C"));
        shadedZones.addAll(getZonesInsideAllContours(POWER_REGION_ABCD, "A", "D"));
        shadedZones.addAll(getZonesInsideAllContours(getZonesOutsideContours(POWER_REGION_ABCD, "A"), "B"));
        shadedZones.addAll(getZonesInsideAllContours(getZonesOutsideContours(POWER_REGION_ABCD, "C"), "D"));

        ArrayList<Zone> presentZones = new ArrayList<>(POWER_REGION_ABCD);
        presentZones.removeAll(shadedZones);

        TreeMap<String, Region> habitats = new TreeMap<>();
        habitats.put("s", new Region(
                Zone.fromInContours("C", "D").withOutContours("A", "B"),
                Zone.fromInContours("C").withOutContours("A", "B", "D")
        ));

        return createPrimarySD(habitats.keySet(), habitats, shadedZones, presentZones);
    }

    public static PrimarySpiderDiagram getDiagramABCWhereCContainsA() {
        ArrayList<Zone> zonesInsideAC_outsideC = Zones.getZonesOutsideContours(Zones.getZonesInsideAnyContour(POWER_REGION_ABC, "A", "C"), "C");

        TreeSet<Zone> presentZones = new TreeSet<>(POWER_REGION_ABC);
        presentZones.removeAll(zonesInsideAC_outsideC);

        return SpiderDiagrams.createPrimarySD(null, null, zonesInsideAC_outsideC, presentZones);
    }

    public static PrimarySpiderDiagram getVenn2Diagram(String contour1, String contour2) {
        return SpiderDiagrams.createPrimarySD(null, null, null, Zones.allZonesForContours(contour1, contour2));
    }

    public static PrimarySpiderDiagram getVenn3Diagram(String contour1, String contour2, String contour3) {
        return SpiderDiagrams.createPrimarySD(null, null, null, Zones.allZonesForContours(contour1, contour2, contour3));
    }

    /**
     * See {@link speedith.core.reasoning.util.unitary.TestSpiderDiagrams#getSpiderDiagramSDTFilesCount()} for the
     * number of all spider diagram test files.
     */
    public static SpiderDiagram readSpiderDiagramFromSDTFile(int fileIdx) throws ReadingException, IOException {
        return readSpiderDiagramFromSDTFile(VALID_SPIDER_DIAGRAM_SDT_FILES[fileIdx]);
    }

    /**
     * See {@link speedith.core.reasoning.util.unitary.TestSpiderDiagrams#getSpiderDiagramSDTFilesCount()} for the
     * number of all spider diagram test files.
     */
    public static SpiderDiagram tryReadSpiderDiagramFromSDTFile(int fileIdx) {
        try {
            return readSpiderDiagramFromSDTFile(fileIdx);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static SpiderDiagram readSpiderDiagramFromSDTFile(String fileTitle) throws ReadingException, IOException {
        String fullPath = "/speedith/core/lang/reader/" + fileTitle;
        return SpiderDiagramsReader.readSpiderDiagram(GoalsTest.getSpiderDiagramTestFile(fullPath));
    }

    public static int getSpiderDiagramSDTFilesCount() {
        return VALID_SPIDER_DIAGRAM_SDT_FILES.length;
    }
}