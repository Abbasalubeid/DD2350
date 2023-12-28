import java.util.*;
import java.util.stream.Collectors;

public class CastingSolver {
    private int totalRoles;
    private int totalScenes;
    private int totalActors;
    private List<Actor> actors;
    private boolean[][] roleConflicts;
    private List<Scene> scenes;
    private HashSet<Integer> roles;

    class Actor {
        int number;
        List<Integer> roles;

        Actor(int number) {
            this.number = number;
            this.roles = new ArrayList<>();
        }

        void addRole(int role) {
            this.roles.add(role);
        }
    }

    class Scene {
        Set<Integer> rolesInScene;

        Scene() {
            rolesInScene = new HashSet<>();
        }

        void addRole(int role) {
            rolesInScene.add(role);
        }
    }

    public CastingSolver() {
        parseInput();
    }

    private void parseInput() {
        Scanner scanner = new Scanner(System.in);
        totalRoles = scanner.nextInt();
        totalScenes = scanner.nextInt();
        totalActors = scanner.nextInt();

        actors = new ArrayList<>();
        scenes = new ArrayList<>();
        roles = new HashSet<>();
        roleConflicts = new boolean[totalRoles + 1][totalRoles + 1];

        for (int i = 0; i < totalActors; i++) {
            actors.add(new Actor(i + 1));
        }

        for (int i = 0; i < totalRoles; i++) {
            int totalActorsForRole = scanner.nextInt();
            for (int j = 0; j < totalActorsForRole; j++) {
                int actor = scanner.nextInt();
                actors.get(actor - 1).addRole(i + 1);
            }
        }

        for (int i = 0; i < totalScenes; i++) {
            int totalRolesInScene = scanner.nextInt();
            Scene scene = new Scene();
            scenes.add(scene);
            for (int j = 0; j < totalRolesInScene; j++) {
                int role = scanner.nextInt();
                scene.addRole(role);
                roles.add(role);
            }
        }

        createRoleConflicts();
        scanner.close();
    }

    private void createRoleConflicts() {
        for (Scene scene : scenes) {
            for (int role1 : scene.rolesInScene) {
                for (int role2 : scene.rolesInScene) {
                    roleConflicts[role1][role2] = true;
                    roleConflicts[role2][role1] = true;
                }
            }
        }
    }

    private boolean isValid(int[] solution, int role, int actor) {

        for (int i = 1; i <= totalRoles; i++) {
            
            if (solution[i] == actor && roleConflicts[i][role]) {
                return false;
            }
            if ((solution[i] == 1 && actor == 2 || solution[i] == 2 && actor == 1) && roleConflicts[i][role]) {
                return false;
            }
        }
        return true;
    }

    private int[] solve() {
        int[] solution = new int[totalRoles + 1];
        boolean foundPair = false;

        for (int role1 : actors.get(0).roles) {
            if (foundPair) {
                break;
            }

            for (int role2 : actors.get(1).roles) {
                if (!roleConflicts[role1][role2]) {
                    solution[role1] = 1;
                    solution[role2] = 2;
                    foundPair = true;
                    break;
                }
            }
        }

        int superActor = totalActors + 1;
        for (int i = 1; i <= totalRoles; i++) {
            if (solution[i] == 0) {
                solution[i] = superActor++;
            }
        }
        return solution;
    }

    private int[] heuristic(int[] solution) {
        int[] tempSolution = Arrays.copyOf(solution, solution.length);
        for (int i = 1; i <= totalRoles; i++) {
            if (solution[i] <= totalActors) {
                // System.out.println("original actor got this role (continue). Actor: " +
                // solution[i] + " role:" + i);
                continue;
            } // Skip if it is not a superactor

            for (Actor actor : actors) {
                // System.out.println("Can actor " + actor.number + " take role " + i + "
                // insetad of super actor " + solution[i]);
                if (actor.roles.contains(i) && isValid(tempSolution, i, actor.number)) {
                    // System.out.println("Yes, actor " + actor.number + " can take role " + i + "
                    // insetad of super actor " + solution[i]);
                    // System.out.println("Old solution " + Arrays.toString(solution));
                    tempSolution[i] = actor.number;
                    // System.out.println("New Solution " + Arrays.toString(tempSolution));
                    break;
                }
            }
        }
        return tempSolution;
    }

    private void printSolution(int[] solution) {
        Map<Integer, List<Integer>> actorRolesMap = new HashMap<>();
        
        int counter = 1;
        for (int role = 1; role <= totalRoles; role++) {
            int actorNumber = solution[role];
            if (!actorRolesMap.containsKey(actorNumber)) {
                if (actorNumber > totalActors) {
                    actorNumber = totalActors + counter++;
                }
                actorRolesMap.put(actorNumber, new ArrayList<>());
            }
            actorRolesMap.get(actorNumber).add(role);
        }

        System.out.println(actorRolesMap.size());

        for (Map.Entry<Integer, List<Integer>> entry : actorRolesMap.entrySet()) {
            System.out.print(entry.getKey() + " " + entry.getValue().size());
            for (int i : entry.getValue()) {
                System.out.print(" " + i);
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        CastingSolver solver = new CastingSolver();
        int[] initialSolution = solver.solve();
        int[] improvedSolution = initialSolution;
            improvedSolution = solver.heuristic(improvedSolution);
        // System.out.println(Arrays.toString(improvedSolution));

        solver.printSolution(improvedSolution);
    }
}