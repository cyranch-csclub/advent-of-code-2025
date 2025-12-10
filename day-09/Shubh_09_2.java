import java.util.*;
import java.io.*;

public class Shubh_09_2 {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("input/d9s.in"));
        in.useDelimiter("[,\\s]+");
        ArrayList<Coord> coords = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();

        while (in.hasNext()) {
            long x = in.nextLong();
            long y = in.nextLong();
            coords.add(new Coord(x, y));
            if (coords.size() > 1) {
                edges.add(new Edge(coords.get(coords.size() - 2), coords.get(coords.size() - 1)));
            }
        }
        edges.add(new Edge(coords.get(coords.size() - 1), coords.get(0)));

        // find the max length of the perimiter between any two points
        // long maxPerim = 0;
        // long sumPerim = 0;
        // for (int i = 0; i < coords.size(); i++) {
        // for (int j = i + 1; j < coords.size(); j++) {
        // Coord c1 = coords.get(i);
        // Coord c2 = coords.get(j);

        // long perim = c1.perim(c2);
        // if (perim > maxPerim) {
        // maxPerim = perim;
        // }
        // sumPerim += perim;
        // }
        // }
        // System.out.println(maxPerim);
        // --> 276060
        // System.out.println(sumPerim);
        // --> 19143800464 :(
        // won't work

        long maxArea = -1;
        long ai = -1;
        long bi = -1;
        for (int i = 0; i < coords.size(); i++) {
            big: for (int j = i + 1; j < coords.size(); j++) {
                Coord c1 = coords.get(i);
                Coord c2 = coords.get(j);

                long minX = Math.min(c1.x, c2.x);
                long maxX = Math.max(c1.x, c2.x);
                long minY = Math.min(c1.y, c2.y);
                long maxY = Math.max(c1.y, c2.y);

                // skip if cant beat max area (area calc is faster)
                long currentArea = (maxX - minX + 1) * (maxY - minY + 1);
                if (currentArea <= maxArea)
                    continue;

                // check if edges cut through rectangle
                for (Edge e : edges) {
                    if (e.from.x == e.to.x) {
                        // vert edge
                        if (e.from.x > minX && e.from.x < maxX) {
                            // edge is strictly inside rect's x range
                            long edgeMinY = Math.min(e.from.y, e.to.y);
                            long edgeMaxY = Math.max(e.from.y, e.to.y);
                            if (Math.max(minY, edgeMinY) < Math.min(maxY, edgeMaxY)) {
                                // overlap in y ranges
                                continue big;
                            }
                        }
                    } else {
                        // horiz
                        if (e.from.y > minY && e.from.y < maxY) {
                            // edge is strictly inside rect's y range
                            long edgeMinX = Math.min(e.from.x, e.to.x);
                            long edgeMaxX = Math.max(e.from.x, e.to.x);
                            if (Math.max(minX, edgeMinX) < Math.min(maxX, edgeMaxX)) {
                                // edge overlaps in x ranges
                                continue big;
                            }
                        }
                    }
                }

                // check if center of rect is inside polygon
                double midX = (minX + maxX) / 2.0;
                double midY = (minY + maxY) / 2.0;
                int hits = 0;

                for (Edge e : edges) {
                    // only vertical edges can be hit by a horizontal ray
                    if (e.from.x == e.to.x) {
                        double y1 = Math.min(e.from.y, e.to.y);
                        double y2 = Math.max(e.from.y, e.to.y);
                        if (midY > y1 && midY < y2) {
                            if (e.from.x > midX) {
                                hits++;
                            }
                        }
                    }
                }

                if (hits % 2 == 0) {
                    continue big;
                }

                hits = 0;
                // same thing but for vert rays
                for (Edge e : edges) {
                    // only horiz edges can be hit by a vert ray
                    if (e.from.y == e.to.y) {
                        double x1 = Math.min(e.from.x, e.to.x);
                        double x2 = Math.max(e.from.x, e.to.x);
                        if (midX > x1 && midX < x2) {
                            if (e.from.y > midY) {
                                hits++;
                            }
                        }
                    }
                }

                if (hits % 2 == 0) {
                    continue big;
                }

                // now shift the point slightly
                midX += 0.67;
                midY += 0.67;
                hits = 0;

                for (Edge e : edges) {
                    // only vertical edges can be hit by a horizontal ray
                    if (e.from.x == e.to.x) {
                        double y1 = Math.min(e.from.y, e.to.y);
                        double y2 = Math.max(e.from.y, e.to.y);
                        if (midY > y1 && midY < y2) {
                            if (e.from.x > midX) {
                                hits++;
                            }
                        }
                    }
                }

                if (hits % 2 == 0) {
                    continue big;
                }

                hits = 0;
                // same thing but for vert rays
                for (Edge e : edges) {
                    // only horiz edges can be hit by a vert ray
                    if (e.from.y == e.to.y) {
                        double x1 = Math.min(e.from.x, e.to.x);
                        double x2 = Math.max(e.from.x, e.to.x);
                        if (midX > x1 && midX < x2) {
                            if (e.from.y > midY) {
                                hits++;
                            }
                        }
                    }
                }

                if (hits % 2 == 0) {
                    continue big;
                }

                // safe to say that rectangle is fully inside polygon
                ai = i;
                bi = j;
                maxArea = currentArea;
            }
        }
        System.out.println(ai + " " + bi);
        System.out.println(coords.get((int) ai) + " " + coords.get((int) bi));
        System.out.println(maxArea);
    }

    record Coord(long x, long y) {
        long perim(Coord o) {
            long mnx = Math.min(x, o.x);
            long mxx = Math.max(x, o.x);
            long mny = Math.min(y, o.y);
            long mxy = Math.max(y, o.y);

            return 2 * ((mxx - mnx + 1) + (mxy - mny + 1));
        }

        long area(Coord o) {
            long mnx = Math.min(x, o.x);
            long mxx = Math.max(x, o.x);
            long mny = Math.min(y, o.y);
            long mxy = Math.max(y, o.y);
            // +1 bc if they are the same coordinate, area is 1, bc they're cells
            return (mxx - mnx + 1) * (mxy - mny + 1);
        }
    }

    record Edge(Coord from, Coord to) {
        long length() {
            long dx = to.x - from.x;
            long dy = to.y - from.y;
            // bc one of them will be 0
            return Math.abs(dx) + Math.abs(dy);
        }
    }
}