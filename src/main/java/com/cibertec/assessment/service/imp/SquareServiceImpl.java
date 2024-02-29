package com.cibertec.assessment.service.imp;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cibertec.assessment.beans.PolygonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.repo.SquareRepo;
import com.cibertec.assessment.service.PolygonService;
import com.cibertec.assessment.service.SquareService;

@Service
public class SquareServiceImpl implements SquareService{

	@Autowired 
	SquareRepo squareRepo;
	
	@Autowired
	PolygonService polygonService;

	@Override
	public Square create(Square s) {

		// Armo los poligonos en base a los puntos x e y
		Polygon square = createPolygon(s.getXpoints(), s.getYpoints());
		List<com.cibertec.assessment.model.Polygon> polygonModels = polygonService.list();
		List<Polygon> polygons = new ArrayList<>();
		for(com.cibertec.assessment.model.Polygon polygon: polygonModels){
			polygons.add(createPolygon(polygon.getXpoints(), polygon.getYpoints()));
		}

		// Buscar intercepcion de los poligonos existentes con el cuadrado (+)
		List<Polygon> intersectingPolygons = findIntersectingPolygons(square, polygons);

		List<Integer> idPoligonos = new ArrayList<>();

		// Guardar los id de los poligonos que interceptan
		for (Polygon polygon : intersectingPolygons) {

			for(com.cibertec.assessment.model.Polygon listPolygon: polygonModels){
				// Busco el id de los poligonos en base a sus coordenadas
				if(listPolygon.getXpoints().equals(formatXPoints(polygon)) &&
						listPolygon.getYpoints().equals(formatYPoints(polygon))
				){
					idPoligonos.add(listPolygon.getId());
				}
			}
		}

		// Eliminar los id duplicados
		List<Integer> idPoligonosSinDuplicados = idPoligonos.stream().distinct().collect(Collectors.toList());

		// Ordenar de forma ascendente los id
		Collections.sort(idPoligonosSinDuplicados);

		// Convertir a una cadena en el formato solicitado
		String idPoligonosString = "[" + idPoligonosSinDuplicados.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(", ")) + "]";

		// Se actualiza el id de los poligonos en el objeto
		s.setPolygons(idPoligonosString);

		//Se guarda en la base de datos
		return squareRepo.save(s);
	}

	private static String formatXPoints(Polygon polygon) {
		int[] xPoints = polygon.xpoints;
		StringBuilder xPointsStr = new StringBuilder("[");

		for (int i = 0; i < xPoints.length; i++) {
			xPointsStr.append(xPoints[i]);
			if (i < xPoints.length - 1) {
				xPointsStr.append(", ");
			}
		}

		xPointsStr.append("]");

		return xPointsStr.toString();
	}

	private static String formatYPoints(Polygon polygon) {
		int[] yPoints = polygon.ypoints;
		StringBuilder yPointsStr = new StringBuilder("[");

		for (int i = 0; i < yPoints.length; i++) {
			yPointsStr.append(yPoints[i]);
			if (i < yPoints.length - 1) {
				yPointsStr.append(", ");
			}
		}

		yPointsStr.append("]");

		return yPointsStr.toString();
	}

	@Override
	public List<Square> list() {
		return squareRepo.findAll();
	}

	private static Polygon createPolygon(String xPointsStr, String yPointsStr) {
		int[] xPoints = convertStringToIntArray(xPointsStr);
		int[] yPoints = convertStringToIntArray(yPointsStr);
		return new Polygon(xPoints, yPoints, xPoints.length);
	}

	private static int[] convertStringToIntArray(String str) {
		String[] strArray = str.replaceAll("\\[|\\]", "").split(", ");
		int[] intArray = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			intArray[i] = Integer.parseInt(strArray[i]);
		}
		return intArray;
	}

	private static List<Polygon> findIntersectingPolygons(Polygon square, List<Polygon> polygons) {
		List<Polygon> intersectingPolygons = new ArrayList<>();
		for (Polygon polygon : polygons) {
			if (hasIntersectingSides(square, polygon)) {
				intersectingPolygons.add(polygon);
			}
		}
		return intersectingPolygons;
	}

	private static boolean hasIntersectingSides(Polygon polygon1, Polygon polygon2) {
		for (int i = 0; i < polygon1.npoints; i++) {
			for (int j = 0; j < polygon2.npoints; j++) {
				if (lineIntersect(polygon1.xpoints[i], polygon1.ypoints[i],
						polygon1.xpoints[(i + 1) % polygon1.npoints], polygon1.ypoints[(i + 1) % polygon1.npoints],
						polygon2.xpoints[j], polygon2.ypoints[j],
						polygon2.xpoints[(j + 1) % polygon2.npoints], polygon2.ypoints[(j + 1) % polygon2.npoints])) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean lineIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		int den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (den == 0) {
			return false;
		}
		int numX = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
		int numY = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4);
		double intersectionX = (double) numX / den;
		double intersectionY = (double) numY / den;

		return isBetween(intersectionX, x1, x2) && isBetween(intersectionY, y1, y2)
				&& isBetween(intersectionX, x3, x4) && isBetween(intersectionY, y3, y4);
	}

	private static boolean isBetween(double value, double bound1, double bound2) {
		return value >= Math.min(bound1, bound2) && value <= Math.max(bound1, bound2);
	}

}
