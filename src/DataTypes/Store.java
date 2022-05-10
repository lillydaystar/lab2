package DataTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Store implements Iterable<Group> {

    private final List<Group> groups;

    public Store(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public Store() {
        this.groups = new ArrayList<>();
    }

    public void add(Group group) {
        this.groups.add(group);
    }

    public int numberOfGroups() {
        return this.groups.size();
    }

    public Group get(int number) throws IncorrectGroupException {
        if (number >= 0 && number < this.groups.size())
            return this.groups.get(number);
        else throw new IncorrectGroupException("Group position "+number
                +" is out of bounds for this store");
    }

    public void remove(int position) throws IncorrectGroupException {
        if (position >= 0 && position < this.groups.size())
            this.groups.remove(position);
        else throw new IncorrectGroupException("Group position is out of bounds for this store");
    }

    public void set(int position, Group newGroup) throws IncorrectGroupException {
        if (position >= 0 && position < this.groups.size()) {
            if (newGroup != null && newGroup.getName() != null && newGroup.getDescription() != null)
                this.groups.set(position, newGroup);
            else throw new IncorrectGroupException("Incorrect group! (not all fields initialized)");
        }
        else throw new IncorrectGroupException("Group position is out of bounds for this store");
    }

    public boolean fileExist(File file){
        return groups.stream().map(Group::getFile).anyMatch(f -> f.equals(file));
    }

    public boolean isEmpty() {
        return this.groups.isEmpty();
    }

    @Override
    public Iterator<Group> iterator() {
        return new Iterator<Group>() {
            int position = 0;
            @Override
            public boolean hasNext() {
                return position < groups.size();
            }
            @Override
            public Group next() {
                return groups.get(position++);
            }
        };
    }
}