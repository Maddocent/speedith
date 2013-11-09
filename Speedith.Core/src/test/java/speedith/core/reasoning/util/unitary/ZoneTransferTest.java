package speedith.core.reasoning.util.unitary;

import org.junit.Test;
import speedith.core.lang.PrimarySpiderDiagram;
import speedith.core.lang.Zone;
import speedith.core.lang.Zones;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static speedith.core.lang.SpiderDiagrams.createPrimarySD;
import static speedith.core.reasoning.util.unitary.TestSpiderDiagrams.vennABCZones;

public class ZoneTransferTest {

    private static final ArrayList<Zone> vennABDZones = Zones.allZonesForContours("A", "B", "D");
    private static final ArrayList<Zone> intersectionAC = Zones.getZonesInsideAllContours(vennABCZones, "A", "C");
    private static final PrimarySpiderDiagram diagramWithABZones = getDiagramWithABZones();
    private static final PrimarySpiderDiagram diagramWithBCZones = getDiagramWithBCZones();
    public static final PrimarySpiderDiagram vennABCDiagram = createPrimarySD(null, null, null, vennABCZones);
    public static final PrimarySpiderDiagram vennABDDiagram = createPrimarySD(null, null, null, vennABDZones);
    public static final PrimarySpiderDiagram diagramABC_shadedSetAC = createPrimarySD(null, null, intersectionAC, vennABCZones);
    public static final PrimarySpiderDiagram diagramABC_shadedSetC_A = createPrimarySD(null, null, Zones.getZonesOutsideContours(Zones.getZonesInsideAnyContour(vennABCZones, "A", "C"), "A"), vennABCZones);
    public static final PrimarySpiderDiagram diagramSpeedithPaperD2 = TestSpiderDiagrams.getDiagramSpeedithPaperD2();

    @Test
    public void contoursOnlyInSource_returns_the_contour_that_is_present_in_the_source_diagram_but_not_in_the_destination_diagram() throws Exception {
        ZoneTransfer zoneTransfer = new ZoneTransfer(diagramWithABZones, diagramWithBCZones);
        assertThat(
                zoneTransfer.contoursOnlyInSource(),
                contains("A")
        );
    }

    @Test
    public void contoursOnlyInSource_returns_an_empty_set_when_the_source_diagram_has_no_contours() throws Exception {
        ZoneTransfer zoneTransfer = new ZoneTransfer(createPrimarySD(), diagramWithBCZones);
        assertThat(
                zoneTransfer.contoursOnlyInSource(),
                hasSize(0)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void destinationZonesForSourceContour_should_throw_an_exception_if_the_contour_is_not_in_the_source_diagram() {
        new ZoneTransfer(diagramWithABZones, diagramWithBCZones).zonesInDestinationOutsideContour("D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void destinationZonesForSourceContour_should_throw_an_exception_if_the_contour_is_in_the_destination_diagram() {
        new ZoneTransfer(diagramWithABZones, diagramWithBCZones).zonesInDestinationOutsideContour("B");
    }

    @Test
    public void zonesInDestinationOutsideContour_should_return_an_empty_set_for_two_Venn_diagrams() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(vennABCDiagram, vennABDDiagram);
        assertThat(
                zoneTransfer.zonesInDestinationOutsideContour("C"),
                hasSize(0)
        );
    }

    @Test
    public void zonesInDestinationOutsideContour_should_return_all_zones_inside_A_when_A_is_disjoint_to_C() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(diagramABC_shadedSetAC, vennABDDiagram);
        assertThat(
                zoneTransfer.zonesInDestinationOutsideContour("C"),
                containsInAnyOrder(Zones.getZonesInsideAllContours(vennABDZones, "A").toArray())
        );
    }

    @Test
    public void zonesInDestinationOutsideContour_should_return_all_zones_outside_A_when_A_is_contains_C() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(diagramABC_shadedSetC_A, vennABDDiagram);
        assertThat(
                zoneTransfer.zonesInDestinationOutsideContour("C"),
                containsInAnyOrder(Zones.getZonesOutsideContours(vennABDZones, "A").toArray())
        );
    }

    @Test
    public void zonesInDestinationOutsideContour_when_using_diagrams_from_speedith_paper_should_return_zones_outside_E() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(diagramSpeedithPaperD2, TestSpiderDiagrams.diagramSpeedithPaperD1);
        assertThat(
                zoneTransfer.zonesInDestinationOutsideContour("E"),
                containsInAnyOrder(
                        Zone.fromInContours("B").withOutContours("A", "D", "C"),
                        Zone.fromInContours("B", "D").withOutContours("A", "C"),
                        Zone.fromInContours("D").withOutContours("A", "B", "C"),
                        Zone.fromOutContours("A", "C", "B", "D")
                )
        );
    }

    @Test
    public void zonesInDestinationInsideContour_should_return_an_empty_region_in_a_venn2_diagram() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(vennABCDiagram, vennABDDiagram);
        assertThat(
                zoneTransfer.zonesInDestinationInsideContour("C"),
                hasSize(0)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void zonesInDestinationInsideContour_should_throw_an_exception_if_the_contour_is_not_only_in_the_source_diagram() {
        new ZoneTransfer(vennABCDiagram, vennABDDiagram).zonesInDestinationInsideContour("A");
    }

    @Test
    public void zonesInDestinationInsideContour_should_return_an_empty_region_in_a_venn2_diagram1() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(vennABCDiagram, vennABDDiagram);
        assertThat(
                zoneTransfer.zonesInDestinationInsideContour("C"),
                hasSize(0)
        );
    }

    @Test
    public void zonesInDestinationInsideContour_should_return_zones_inside_A() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(TestSpiderDiagrams.getDiagramABCWhereCContainsA(), vennABDDiagram);
        assertThat(
                zoneTransfer.zonesInDestinationInsideContour("C"),
                containsInAnyOrder(
                        Zone.fromInContours("A").withOutContours("B", "D"),
                        Zone.fromInContours("A", "B").withOutContours("D"),
                        Zone.fromInContours("A", "D").withOutContours("B"),
                        Zone.fromInContours("A", "B", "D").withOutContours()
                )
        );
    }

    @Test
    public void zonesInDestinationInsideContour_when_transferring_contour_E_in_diagrams_from_speedith_paper_should_return_zones_inside_AC() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(diagramSpeedithPaperD2, TestSpiderDiagrams.diagramSpeedithPaperD1);
        assertThat(
                zoneTransfer.zonesInDestinationInsideContour("E"),
                containsInAnyOrder(
                        Zone.fromInContours("A", "C").withOutContours("B", "D")
                )
        );
    }

    @Test
    public void zonesInDestinationInsideContour_when_transferring_contour_E_in_a_diagram_where_E_contains_A_and_C_should_return_all_zones_inside_A() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(TestSpiderDiagrams.getDiagramSpeedithPaperD2("E", "A"), TestSpiderDiagrams.diagramSpeedithPaperD1);
        assertThat(
                zoneTransfer.zonesInDestinationInsideContour("E"),
                containsInAnyOrder(
                        Zone.fromInContours("A", "C").withOutContours("B", "D"),
                        Zone.fromInContours("A", "D").withOutContours("B", "C"),
                        Zone.fromInContours("A").withOutContours("B", "C", "D")
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferContour_should_throw_an_exception_if_the_source_diagram_does_not_contain_the_contour_to_transfer() {
        new ZoneTransfer(diagramWithABZones, createPrimarySD()).transferContour("C");
    }

    @Test
    public void transferContour_into_an_empty_primary_diagram_should_return_a_diagram_with_a_single_zone() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(TestSpiderDiagrams.getVenn2Diagram("A", "B"), TestSpiderDiagrams.getVenn2Diagram("B", "C"));
        assertThat(
                zoneTransfer.transferContour("A"),
                equalTo(createPrimarySD(null, null, null, Zones.allZonesForContours("A", "B", "C")))
        );
    }

    @Test
    public void transferContour_when_adding_a_contour_to_a_venn_diagram_should_produce_the_same_diagram() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(TestSpiderDiagrams.getDiagramABCWhereCContainsA(), TestSpiderDiagrams.getVenn2Diagram("B", "C"));
        assertThat(
                zoneTransfer.transferContour("A"),
                equalTo(TestSpiderDiagrams.getDiagramABCWhereCContainsA())
        );
    }

    @Test
    public void transferContour_should_create_some_shading() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(TestSpiderDiagrams.getDiagramABCWhereCContainsA(), vennABDDiagram);
        ArrayList<Zone> vennABCDZones = Zones.allZonesForContours("A", "B", "C", "D");
        ArrayList<Zone> presentZones = new ArrayList<>(Zones.sameRegionWithNewContours(vennABDZones, "C"));
        presentZones.removeAll(Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(presentZones, "C"), "A"));
        ArrayList<Zone> shadedZones = Zones.getZonesInsideAllContours(Zones.getZonesOutsideContours(vennABCDZones, "C"), "A");
        assertThat(
                zoneTransfer.transferContour("C"),
                equalTo(createPrimarySD(null, null, shadedZones, presentZones))
        );
    }

    @Test
    public void transferContour_should_create_replicate_the_example_in_Fig2() {
        ZoneTransfer zoneTransfer = new ZoneTransfer(diagramSpeedithPaperD2, TestSpiderDiagrams.diagramSpeedithPaperD1);

        PrimarySpiderDiagram expectedDiagram = TestSpiderDiagrams.getDiagramD1PrimeFromSpeedithPaper();

        PrimarySpiderDiagram diagramWithTransferredContour = zoneTransfer.transferContour("E");

        assertThat(
                diagramWithTransferredContour,
                equalTo(expectedDiagram)
        );
    }

    private static PrimarySpiderDiagram getDiagramWithBCZones() {
        return createPrimarySD(null, null, null, asList(Zone.fromOutContours("B", "C")));
    }

    private static PrimarySpiderDiagram getDiagramWithABZones() {
        return createPrimarySD(null, null, null, asList(Zone.fromInContours("A", "B")));
    }
}
